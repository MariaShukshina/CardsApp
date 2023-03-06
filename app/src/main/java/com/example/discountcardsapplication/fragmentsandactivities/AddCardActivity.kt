package com.example.discountcardsapplication.fragmentsandactivities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.example.discountcardsapplication.R
import com.example.discountcardsapplication.databinding.ActivityAddOrEditCardBinding
import com.example.discountcardsapplication.fragmentsandactivities.ChooseCompanyActivity.Companion.COMPANY_IMAGE
import com.example.discountcardsapplication.fragmentsandactivities.ChooseCompanyActivity.Companion.COMPANY_NAME
import com.example.discountcardsapplication.fragmentsandactivities.ScanCardActivity.Companion.BARCODE_FORMAT
import com.example.discountcardsapplication.fragmentsandactivities.ScanCardActivity.Companion.CODE
import com.example.discountcardsapplication.domain.models.Card
import com.example.discountcardsapplication.domain.models.GeneratedResult
import com.example.discountcardsapplication.domain.utils.CodeGenerator
import com.example.discountcardsapplication.viewmodels.AddCardActivityViewModel
import com.google.zxing.BarcodeFormat
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.BasePermissionListener
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

@AndroidEntryPoint
class AddCardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddOrEditCardBinding
    private var companyName: String = ""
    private var imageResource: Int = 0
    private var customImage: Uri? = null
    private var barcodeFormat: BarcodeFormat? = null
    private var code: String? = null
    private var isCameraPermissionRequested = false
    private lateinit var mSharedPreferences: SharedPreferences
    private var isReadExternalStoragePermissionRequested = false

    private val addCardActivityViewModel by viewModels<AddCardActivityViewModel>()

    private val startScanActivityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            handleScannedData(result)
        }
    private val startGalleryActivityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            handleGalleryActivityData(result)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mSharedPreferences = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        isCameraPermissionRequested =
            mSharedPreferences.getBoolean(IS_CAMERA_PERMISSION_CHECKED, false)
        isReadExternalStoragePermissionRequested = mSharedPreferences.getBoolean(
            IS_EXTERNAL_STORAGE_PERMISSION_CHECKED,
            false
        )

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

        tryToHideScanButton()
        tryToHideChooseFromGalleryButton()
    }

    private fun handleScannedData(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            if (intent != null) {
                barcodeFormat = intent.getSerializableExtra(BARCODE_FORMAT) as BarcodeFormat
                code = intent.getStringExtra(CODE)!!
                binding.etCardNumber.setText(code)
                binding.etCardNumber.isFocusableInTouchMode = false
                Log.i("AddOrEditCardActivity", barcodeFormat.toString())
            }
            setupButtonDoneColor()
        }
    }

    private fun handleGalleryActivityData(result: ActivityResult) {
        if (result.resultCode != Activity.RESULT_OK) return
        val intent = result.data ?: return
        try {
            val selectedImageBitmap: Bitmap =
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(this.contentResolver, intent.data)
                } else {
                    ImageDecoder.decodeBitmap(
                        ImageDecoder.createSource(
                            this.contentResolver,
                            intent.data!!
                        )
                    )
                }
            customImage = saveImageToInternalStorage(selectedImageBitmap)

            Log.i("Saved image: ", "Path :: $customImage")
            binding.newCardImage.scaleType = ImageView.ScaleType.CENTER_CROP
            binding.newCardImage.setImageBitmap(selectedImageBitmap)
        } catch (e: IOException) {
            Log.e(LOG_TAG, e.toString())
            Toast.makeText(
                this@AddCardActivity,
                "Failed to load image from Gallery.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun tryToHideScanButton() {
        if (ContextCompat.checkSelfPermission(
                this@AddCardActivity,
                Manifest.permission.CAMERA
            )
            == PackageManager.PERMISSION_DENIED && isCameraPermissionRequested
        ) {
            binding.scanIcon.visibility = View.GONE
        }
    }

    private fun tryToHideChooseFromGalleryButton() {
        if (ContextCompat.checkSelfPermission(
                this@AddCardActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            == PackageManager.PERMISSION_DENIED && isReadExternalStoragePermissionRequested
        ) {
            binding.buttonLoadYourPhoto.visibility = View.GONE
        }
    }

    private fun saveCardToDatabase() {
        when {
            binding.etCompanyName.text.isNullOrEmpty() -> {
                Toast.makeText(
                    this,
                    "Please enter a company name.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            binding.etCardNumber.text.isNullOrEmpty() -> {
                Toast.makeText(
                    this,
                    "Please scan your card or enter " +
                            "a card number manually.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {
                if (barcodeFormat != null && code != null) {
                    val card = createCard()
                    addCardActivityViewModel.insertCard(card)
                    finish()
                } else {
                    createCardManually {
                        addCardActivityViewModel.insertCard(it)
                        finish()
                    }
                }
            }
        }
    }

    private fun createCard(): Card {
        val card = Card(
            id = 0,
            companyName = binding.etCompanyName.text.toString(),
            barcodeFormat = barcodeFormat,
            qrOrBarCode = code,
        )
        if (customImage != null) {
            card.customImage = customImage.toString()
        } else if (imageResource != 0) {
            card.imageResource = imageResource
        } else {
            card.imageResource = R.drawable.ic_placeholder
        }
        return card
    }

    private fun createCardManually(createdCardHandler: (card: Card) -> Unit) {
        val generatedResultsList = ArrayList<GeneratedResult>()
        for (barcodeFormat in BARCODE_FORMAT_LIST) {
            val generatedResult = CodeGenerator().generateQROrBarcodeImage(
                binding.etCardNumber.text.toString(),
                barcodeFormat
            )
            if (generatedResult.bitmap != null) {
                generatedResultsList.add(generatedResult)
            }
        }
        if (generatedResultsList.isNotEmpty()) {
            openChooseBarcodeFormatDialog(generatedResultsList, createdCardHandler)
        } else {
            Toast.makeText(
                this@AddCardActivity,
                "Please check the code you've entered.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun openChooseBarcodeFormatDialog(list: ArrayList<GeneratedResult>, createdCardHandler: (card: Card) -> Unit){
        val chooseBarcodeFormatCustomDialog =
            ChooseBarcodeFormatCustomDialog(this, list) {
                barcodeFormat = it.barcodeFormat
                code = it.code
                val card = createCard()
                createdCardHandler.invoke(card)
            }
        chooseBarcodeFormatCustomDialog.show()
        chooseBarcodeFormatCustomDialog.setCanceledOnTouchOutside(false)
    }

    private fun openScanActivity() {
        Dexter.withContext(this@AddCardActivity)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object : BasePermissionListener() {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    if (response != null) {
                        startScanActivityForResult.launch(
                            Intent(
                                this@AddCardActivity,
                                ScanCardActivity::class.java
                            )
                        )
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissionRequest: PermissionRequest?,
                    permissionToken: PermissionToken?
                ) {
                    showRationaleDialogForPermissions(permissionToken)
                    isCameraPermissionRequested = true
                    mSharedPreferences.edit().putBoolean(
                        IS_CAMERA_PERMISSION_CHECKED,
                        isCameraPermissionRequested
                    ).apply()
                    tryToHideScanButton()
                }
            }).onSameThread().check()
    }

    private fun getDataFromIntentAndSetInViews() {
        val intent = intent
        if (intent.hasExtra(COMPANY_NAME) && intent.hasExtra(COMPANY_IMAGE)) {
            companyName = intent.getStringExtra(COMPANY_NAME)!!
            imageResource = intent.getIntExtra(COMPANY_IMAGE, 0)

            binding.newCardImage.setImageResource(imageResource)
            binding.etCompanyName.setText(companyName)
        }
    }

    private fun showRationaleDialogForPermissions(permissionToken: PermissionToken?) {
        AlertDialog.Builder(this).setMessage(
            "Permission required for this feature is denied. " +
                    "It can be enabled in the application settings."
        )
            .setPositiveButton("Enable permission") { _, _ ->
                if (permissionToken != null) {
                    permissionToken.continuePermissionRequest()
                } else {
                    try {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        Log.e(LOG_TAG, e.toString())
                    }
                }
            }.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                permissionToken?.continuePermissionRequest()
            }.show()
    }

    private fun setupButtonDoneColor() {
        if (binding.etCompanyName.text!!.isNotEmpty() &&
            binding.etCardNumber.text!!.isNotEmpty() && barcodeFormat != null
        ) {
            binding.createCardButtonDone.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.light_salmon
                )
            )
        } else {
            binding.createCardButtonDone.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.teal_700
                )
            )
        }
    }

    private fun choosePhotoFromGallery() {
        Dexter.withContext(this@AddCardActivity).withPermission(
            Manifest.permission.READ_EXTERNAL_STORAGE
        ).withListener(object : BasePermissionListener() {
            override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                if (response != null) {
                    val galleryIntent = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                    startGalleryActivityForResult.launch(galleryIntent)
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissionRequest: PermissionRequest?,
                permissionToken: PermissionToken?
            ) {
                showRationaleDialogForPermissions(permissionToken)
                isReadExternalStoragePermissionRequested = true
                mSharedPreferences.edit().putBoolean(
                    IS_EXTERNAL_STORAGE_PERMISSION_CHECKED,
                    isReadExternalStoragePermissionRequested
                ).apply()
                tryToHideChooseFromGalleryButton()
            }
        }).onSameThread().check()
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): Uri {
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jgp")

        return saveImage(bitmap, file, BITMAP_QUALITY, Bitmap.CompressFormat.JPEG)
    }

    private fun saveImage(bitmap: Bitmap, file: File, quality: Int, compressFormat: Bitmap.CompressFormat): Uri {
        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(compressFormat, quality, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            Log.e(LOG_TAG, e.toString())
        }
        return Uri.parse(file.absolutePath)
    }

    companion object {
        private val BARCODE_FORMAT_LIST = listOf(
            BarcodeFormat.CODE_39,
            BarcodeFormat.CODE_128,
            BarcodeFormat.EAN_8,
            BarcodeFormat.EAN_13,
            BarcodeFormat.QR_CODE
        )
        private const val BITMAP_QUALITY = 100
        private const val IMAGE_DIRECTORY = "DiscountCardsImages"
        private const val PREFERENCE_NAME = "PermissionCheck"
        private const val IS_CAMERA_PERMISSION_CHECKED = "IS_CAMERA_PERMISSION_CHECKED"
        private const val IS_EXTERNAL_STORAGE_PERMISSION_CHECKED =
            "IS_EXTERNAL_STORAGE_PERMISSION_CHECKED"
        private const val LOG_TAG = "AddOrEditCardActivity"
    }
}
