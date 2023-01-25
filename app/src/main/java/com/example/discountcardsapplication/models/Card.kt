package com.example.discountcardsapplication.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.zxing.BarcodeFormat

@Entity(tableName = "card_information")
data class Card(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var companyName: String,
    var imageResource: Int? = null,
    var customImage: String? = null,
    var isFavorite: Boolean = false,
    var qrOrBarCode: String? = null,
    var barcodeFormat: BarcodeFormat? = null
)
