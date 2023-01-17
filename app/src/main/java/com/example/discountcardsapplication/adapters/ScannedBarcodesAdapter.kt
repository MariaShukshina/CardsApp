package com.example.discountcardsapplication.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.discountcardsapplication.R
import com.example.discountcardsapplication.databinding.BarcodeformatItemBinding
import com.example.discountcardsapplication.models.GeneratedResult


class ScannedBarcodesAdapter(): RecyclerView.Adapter<ScannedBarcodesAdapter.ScannedBarcodesViewHolder>() {
    private var barcodesList = listOf<GeneratedResult>()
    lateinit var onItemClick: (GeneratedResult) -> Unit

    fun setBarcodesList(barcodesList: List<GeneratedResult>){
        this.barcodesList = barcodesList
        notifyDataSetChanged()
    }

    class ScannedBarcodesViewHolder(binding: BarcodeformatItemBinding): RecyclerView.ViewHolder(binding.root){
        val barcodeImage = binding.ivGeneratedBitmap
        val barcodeFormatName = binding.tvBarcodeFormatName
        val scannedInfo = binding.tvScannedInfo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScannedBarcodesViewHolder {
        return ScannedBarcodesViewHolder(
            BarcodeformatItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ScannedBarcodesViewHolder, position: Int) {

        holder.barcodeImage.setImageBitmap(barcodesList[position].bitmap)
        holder.barcodeFormatName.text = barcodesList[position].barcodeFormat.toString()
        holder.scannedInfo.text = barcodesList[position].qrCode
        holder.itemView.setOnClickListener {
            // TODO changing color of background
            //holder.itemView.setBackgroundColor(Color.LTGRAY)
            onItemClick.invoke(barcodesList[position])
        }
    }

    override fun getItemCount(): Int {
        return barcodesList.size
    }
}