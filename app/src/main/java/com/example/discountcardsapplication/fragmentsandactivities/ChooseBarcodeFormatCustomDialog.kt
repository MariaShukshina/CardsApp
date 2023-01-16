package com.example.discountcardsapplication.fragmentsandactivities

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.discountcardsapplication.R
import com.example.discountcardsapplication.adapters.ScannedBarcodesAdapter
import com.example.discountcardsapplication.databinding.ChooseBarcodeformatCustomDialogBinding

class ChooseBarcodeFormatCustomDialog(
    var activity: Activity
    ): Dialog(activity), View.OnClickListener {

    private lateinit var scannedBarcodesAdapter: ScannedBarcodesAdapter

    private lateinit var binding: ChooseBarcodeformatCustomDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ChooseBarcodeformatCustomDialogBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)

        scannedBarcodesAdapter = ScannedBarcodesAdapter()

        val recyclerView = binding.rvFormats
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = scannedBarcodesAdapter

        binding.buttonCancel.setOnClickListener(this)
        binding.buttonDone.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonCancel -> {
                dismiss()
            }
            R.id.buttonDone -> {
                //TODO
                dismiss()
            }
        }
    }

}