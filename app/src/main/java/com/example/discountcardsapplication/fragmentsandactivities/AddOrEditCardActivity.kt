package com.example.discountcardsapplication.fragmentsandactivities

import android.Manifest
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.example.discountcardsapplication.databinding.ActivityAddOrEditCardBinding
import com.example.discountcardsapplication.fragmentsandactivities.ChooseCompanyActivity.Companion.COMPANY_IMAGE
import com.example.discountcardsapplication.fragmentsandactivities.ChooseCompanyActivity.Companion.COMPANY_NAME
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.BasePermissionListener

class AddOrEditCardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddOrEditCardBinding
    private var name: String = ""
    private var image: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddOrEditCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonCancel.setOnClickListener {
            finish()
        }
        binding.scanIcon.setOnClickListener {
            openScanActivity()
        }
        getDataFromIntentAndSetInViews()
    }

    private fun openScanActivity() {
        Dexter.withContext(this@AddOrEditCardActivity)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object: BasePermissionListener() {
                override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse?) {
                    startActivity(Intent(this@AddOrEditCardActivity,
                        ScanCardActivity::class.java))
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissionRequest: PermissionRequest?,
                    permissionToken: PermissionToken?
                ) {
                    showRationaleDialogForPermissions()
                }

            }).onSameThread().check()
    }

    private fun getDataFromIntentAndSetInViews(){
        val intent = intent
        if(intent.hasExtra(COMPANY_NAME) && intent.hasExtra(COMPANY_IMAGE)) {
            name = intent.getStringExtra(COMPANY_NAME)!!
            image = intent.getIntExtra(COMPANY_IMAGE, 0)

            binding.newCardImage.setImageResource(image)
            binding.etCompanyName.setText(name)
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
}