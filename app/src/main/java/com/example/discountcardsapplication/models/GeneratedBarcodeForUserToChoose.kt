package com.example.discountcardsapplication.models

import com.google.zxing.BarcodeFormat

data class GeneratedBarcodeForUserToChoose(
    val barcodeFormat: BarcodeFormat?,
    val code: String?
)
