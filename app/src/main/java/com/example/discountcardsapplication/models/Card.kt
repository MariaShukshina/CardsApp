package com.example.discountcardsapplication.models

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat

data class Card(
    val id: Int,
    val name: String,
    val imageResource: Int? = null,
    val customImage: String? = null,
    val isFavorite: Boolean = false,
    val qrOrBarCode: String? = null,
    val qrOrBarCodeImage: Bitmap? = null,
    val barcodeFormat: BarcodeFormat? = null
)
