package com.example.discountcardsapplication.database

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.zxing.BarcodeFormat

@TypeConverters
class CardTypeConverter {

    @TypeConverter
    fun fromBarcodeFormatToString(attribute: BarcodeFormat?): String {
        if(attribute == null) return ""
        return attribute as String
    }

    @TypeConverter
    fun fromStringToBarcodeFormat(attribute: String?): BarcodeFormat? {
        if(attribute == null) return null
        return attribute as BarcodeFormat
    }

}