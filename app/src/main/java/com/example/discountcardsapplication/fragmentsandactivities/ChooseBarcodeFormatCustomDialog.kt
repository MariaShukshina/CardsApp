package com.example.discountcardsapplication.fragmentsandactivities

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.discountcardsapplication.R
import com.example.discountcardsapplication.adapters.ScannedBarcodesAdapter
import com.example.discountcardsapplication.databinding.ChooseBarcodeformatCustomDialogBinding
import com.example.discountcardsapplication.domain.models.GeneratedBarcodeForUserToChoose
import com.example.discountcardsapplication.domain.models.GeneratedResult
import com.google.zxing.BarcodeFormat

class ChooseBarcodeFormatCustomDialog(
    private var activity: Activity,
    private var generatedResultList: ArrayList<GeneratedResult>,
    private var onDataCollected: (GeneratedBarcodeForUserToChoose) -> Unit
) : Dialog(activity), View.OnClickListener {

    private lateinit var scannedBarcodesAdapter: ScannedBarcodesAdapter
    private lateinit var binding: ChooseBarcodeformatCustomDialogBinding
    private var isItemSelected = false
    private var barcodeFormat: BarcodeFormat? = null
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
            scannedInfo = it.qrCode
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonCancel -> {
                dismiss()
            }
            R.id.buttonDone -> {
                if (isItemSelected) {
                    onDataCollected.invoke(GeneratedBarcodeForUserToChoose(barcodeFormat, scannedInfo))
                    dismiss()
                } else {
                    Toast.makeText(context, "Please choose barcode format", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
