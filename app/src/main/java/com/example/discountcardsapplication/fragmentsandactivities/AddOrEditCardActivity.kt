package com.example.discountcardsapplication.fragmentsandactivities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.*
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.discountcardsapplication.R
import com.example.discountcardsapplication.database.CardsDatabase
import com.example.discountcardsapplication.databinding.ActivityAddOrEditCardBinding
import com.example.discountcardsapplication.fragmentsandactivities.ChooseBarcodeFormatCustomDialog.Companion.SCANNED_BARCODE_FORMAT
import com.example.discountcardsapplication.fragmentsandactivities.ChooseBarcodeFormatCustomDialog.Companion.SCANNED_INFO
import com.example.discountcardsapplication.fragmentsandactivities.ChooseBarcodeFormatCustomDialog.Companion.SEND_SELECTED_INFO
import com.example.discountcardsapplication.fragmentsandactivities.ChooseCompanyActivity.Companion.COMPANY_IMAGE
import com.example.discountcardsapplication.fragmentsandactivities.ChooseCompanyActivity.Companion.COMPANY_NAME
import com.example.discountcardsapplication.fragmentsandactivities.ScanCardActivity.Companion.BARCODE_FORMAT
import com.example.discountcardsapplication.fragmentsandactivities.ScanCardActivity.Companion.CODE
import com.example.discountcardsapplication.models.Card
import com.example.discountcardsapplication.models.GeneratedResult
import com.example.discountcardsapplication.utils.CodeGenerator
import com.example.discountcardsapplication.viewmodels.AddOrEditCardActivityViewModel
import com.example.discountcardsapplication.viewmodels.AddOrEditCardActivityViewModelFactory
import com.google.zxing.BarcodeFormat
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.BasePermissionListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class AddOrEditCardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddOrEditCardBinding
    private var companyName: String = ""
    private var imageResource: Int = 0
    private var customImage: Uri? = null
    private var barcodeFormat: BarcodeFormat? = null
    private var code: String? = null
    private var broadcastReceiver: BroadcastReceiver? = null

    private val viewModel: AddOrEditCardActivityViewModel by lazy {
        val cardsDatabase = CardsDatabase.getInstance(this)
        val factory = AddOrEditCardActivityViewModelFactory(cardsDatabase)
        ViewModelProvider(this, factory)[AddOrEditCardActivityViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddOrEditCardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.etCompanyName.addTextChangedListener {
            setupButtonDoneColor()
        }
        binding.etCardNumber.addTextChangedListener {
            setupButtonDoneColor()
        }
        binding.buttonLoadYourPhoto.setOnClickListener {
            choosePhotoFromGallery()
        }
        binding.createCardButtonCancel.setOnClickListener {
            finish()
        }
        binding.scanIcon.setOnClickListener {
            openScanActivity()
        }
        binding.createCardButtonDone.setOnClickListener {
            saveCardToDatabase()
        }
        getDataFromIntentAndSetInViews()
    }

    private fun saveCardToDatabase() {
            when {
                binding.etCompanyName.text.isNullOrEmpty() -> {
                    Toast.makeText(this, "Please enter a company name.",
                        Toast.LENGTH_SHORT).show()
                }
                binding.etCardNumber.text.isNullOrEmpty() -> {
                    Toast.makeText(this, "Please scan your card or enter " +
                            "a card number manually.",
                        Toast.LENGTH_SHORT).show()
                }
                else -> {
                    if(barcodeFormat != null && code != null) {
                        val card = createAutomaticScannedCard()
                        viewModel.insertCard(card)
                        finish()
                    } else {
                        createCardManually{
                            viewModel.insertCard(it)
                            finish()
                        }
                    }
                }
            }
    }
    private fun createCardManually(createdCardHandler: (card: Card) -> Unit) {
        val barcodeFormatsList = listOf(
            BarcodeFormat.CODE_39,
            BarcodeFormat.CODE_128,
            BarcodeFormat.EAN_8,
            BarcodeFormat.EAN_13,
            BarcodeFormat.QR_CODE
        )
        val generatedResultsList = ArrayList<GeneratedResult>()
        for(barcodeFormat in barcodeFormatsList) {
            val generatedResult = CodeGenerator().generateQROrBarcodeImage(
                binding.etCardNumber.text.toString(),
                barcodeFormat
            )
            if(generatedResult.bitmap != null) {
                generatedResultsList.add(generatedResult)
            }
        }
        val chooseBarcodeFormatCustomDialog = ChooseBarcodeFormatCustomDialog(this, generatedResultsList)
        chooseBarcodeFormatCustomDialog.show()
        chooseBarcodeFormatCustomDialog.setCanceledOnTouchOutside(false)

        broadcastReceiver = object: BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == SEND_SELECTED_INFO) {
                    barcodeFormat = intent.getSerializableExtra(SCANNED_BARCODE_FORMAT) as BarcodeFormat?
                    code = intent.getStringExtra(SCANNED_INFO)
                    val card = Card(
                        id = 0,
                        companyName = binding.etCompanyName.text.toString(),
                        barcodeFormat = barcodeFormat,
                        qrOrBarCode = code,
                        description = binding.etDescription.text.toString()
                    )
                    if(customImage != null) {
                        card.customImage = customImage.toString()
                    } else if(imageResource != 0) {
                        card.imageResource = imageResource
                    } else {
                        card.imageResource = R.drawable.ic_placeholder
                    }
                    createdCardHandler.invoke(card)
                } else {
                    Toast.makeText(this@AddOrEditCardActivity,
                        "Please check the code you've entered.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        registerReceiver(broadcastReceiver, IntentFilter(SEND_SELECTED_INFO))
    }

    private fun createAutomaticScannedCard(): Card {
        val card = Card(
            id = 0,
            companyName = binding.etCompanyName.text.toString(),
            barcodeFormat = barcodeFormat,
            qrOrBarCode = code,
            description = binding.etDescription.text.toString()
        )
        if(customImage != null) {
            card.customImage = customImage.toString()
        } else if(imageResource != 0) {
            card.imageResource = imageResource
        } else {
            card.imageResource = R.drawable.ic_placeholder
        }
        return card
    }

    override fun onDestroy() {
        super.onDestroy()
        if(broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver)
        }
    }

    private fun openScanActivity() {
        Dexter.withContext(this@AddOrEditCardActivity)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object: BasePermissionListener() {
                override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse?) {
                    startActivityForResult(Intent(this@AddOrEditCardActivity,
                        ScanCardActivity::class.java), SCAN_CODE_REQUEST_CODE)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissionRequest: PermissionRequest?,
                    permissionToken: PermissionToken?
                ) {
                    showRationaleDialogForPermissions()
                }

            }).onSameThread().check()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == SCAN_CODE_REQUEST_CODE) {
                if(data != null) {
                    barcodeFormat = data.getSerializableExtra(BARCODE_FORMAT) as BarcodeFormat
                    code = data.getStringExtra(CODE)!!
                    binding.etCardNumber.setText(code)
                    Log.i("AddOrrEditCardActivity", barcodeFormat.toString())
                }
                setupButtonDoneColor()
            } else if(requestCode == GALLERY_INTENT) {
                if(data != null) {
                    val contentURI = data.data
                    try {
                        val selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,
                            contentURI)
                        customImage = saveImageToInternalStorage(selectedImageBitmap)

                        Log.e("Saved image: ", "Path :: $customImage")

                        binding.newCardImage.setImageBitmap(selectedImageBitmap)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(this@AddOrEditCardActivity,
                            "Failed to load image from Gallery.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun getDataFromIntentAndSetInViews(){
        val intent = intent
        if(intent.hasExtra(COMPANY_NAME) && intent.hasExtra(COMPANY_IMAGE)) {
            companyName = intent.getStringExtra(COMPANY_NAME)!!
            imageResource = intent.getIntExtra(COMPANY_IMAGE, 0)

            binding.newCardImage.setImageResource(imageResource)
            binding.etCompanyName.setText(companyName)
        }
    }

    private fun showRationaleDialogForPermissions() {
        AlertDialog.Builder(this).setMessage(
            "It looks like you have turned off permission required for this feature. "+
                    "It can be enabled under Application settings")
            .setPositiveButton("GO TO SETTINGS") {
                    _,_ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }.setNegativeButton("Cancel") {
                    dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun setupButtonDoneColor(){
        if(binding.etCompanyName.text!!.isNotEmpty()
            && binding.etCardNumber.text!!.isNotEmpty() && barcodeFormat != null){
            binding.createCardButtonDone.setBackgroundColor(resources.getColor(R.color.light_salmon))
        } else {
            binding.createCardButtonDone.setBackgroundColor(resources.getColor(R.color.teal_700))
        }
    }

    private fun choosePhotoFromGallery() {
        Dexter.withContext(this@AddOrEditCardActivity).withPermissions(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).withListener(object: MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                if(report!!.areAllPermissionsGranted()) {
                    val galleryIntent = Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(galleryIntent, GALLERY_INTENT)
                }
            }
            override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?,
                                                            token: PermissionToken?) {
                showRationaleDialogForPermissions()
            }
        }).onSameThread().check()
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): Uri {
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jgp")

        try {
            val stream : OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return Uri.parse(file.absolutePath)
    }

    companion object {
        private const val SCAN_CODE_REQUEST_CODE = 1
        private const val IMAGE_DIRECTORY = "DiscountCardsImages"
        private const val GALLERY_INTENT = 2
    }
}