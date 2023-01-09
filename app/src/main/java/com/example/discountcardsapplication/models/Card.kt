package com.example.discountcardsapplication.models

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.zxing.BarcodeFormat

@Entity(tableName = "card_information")
data class Card(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val imageResource: Int? = null,
    val customImage: String? = null,
    val isFavorite: Boolean = false,
    val qrOrBarCode: String? = null,
    val barcodeFormat: BarcodeFormat? = null
)
