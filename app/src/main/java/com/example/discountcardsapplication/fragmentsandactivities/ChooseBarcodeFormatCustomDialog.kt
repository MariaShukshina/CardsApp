package com.example.discountcardsapplication.fragmentsandactivities

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.discountcardsapplication.R
import com.example.discountcardsapplication.adapters.ScannedBarcodesAdapter
import com.example.discountcardsapplication.databinding.ChooseBarcodeformatCustomDialogBinding
import com.example.discountcardsapplication.models.GeneratedResult
import com.google.zxing.BarcodeFormat

class ChooseBarcodeFormatCustomDialog(
    var activity: Activity,
    var generatedResultList: ArrayList<GeneratedResult>
    ): Dialog(activity), View.OnClickListener {

    private lateinit var scannedBarcodesAdapter: ScannedBarcodesAdapter
    private lateinit var binding: ChooseBarcodeformatCustomDialogBinding
    private var isItemSelected =  false
    private var barcodeFormat: BarcodeFormat? = null
    private var bitmap: Bitmap? = null
    private var scannedInfo: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ChooseBarcodeformatCustomDialogBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)

        scannedBarcodesAdapter = ScannedBarcodesAdapter()
        scannedBarcodesAdapter.setBarcodesList(generatedResultList)

        val recyclerView = binding.rvFormats
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = scannedBarcodesAdapter

        onItemClick()

        binding.buttonCancel.setOnClickListener(this)
        binding.buttonDone.setOnClickListener(this)
    }

    private fun onItemClick() {
        scannedBarcodesAdapter.onItemClick = {
            isItemSelected = true
            barcodeFormat = it.barcodeFormat
            bitmap = it.bitmap
            scannedInfo = it.qrCode
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_cancel -> {
                dismiss()
            }
            R.id.button_done -> {
                if(isItemSelected) {
                    val intent = Intent()
                    intent.action = SEND_SELECTED_INFO
                    intent.putExtra(SCANNED_INFO, scannedInfo)
                    intent.putExtra(SCANNED_BARCODE_FORMAT, barcodeFormat)
                    activity.sendBroadcast(intent)
                    dismiss()
                } else {
                    Toast.makeText(context, "Please choose barcode format", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        const val SCANNED_BARCODE_FORMAT = "BARCODE_FORMAT"
        const val SCANNED_INFO = "SCANNED_INFO"
        const val SEND_SELECTED_INFO = "SEND_SELECTED_INFO"
    }

}