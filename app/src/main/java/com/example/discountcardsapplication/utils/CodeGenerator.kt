package com.example.discountcardsapplication.utils

import android.graphics.Bitmap
import com.example.discountcardsapplication.models.GeneratedResult
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder

class CodeGenerator {

    fun generateQROrBarcodeImage(inputText: String, barcodeFormat: BarcodeFormat): GeneratedResult {
        return when (barcodeFormat) {
            BarcodeFormat.QR_CODE -> createBitmap(inputText, 200, barcodeFormat)
            BarcodeFormat.EAN_13 -> generateEAN13Image(inputText)
            else -> createBitmap(inputText, 80, barcodeFormat)
        }
    }

    private fun generateEAN13Image(inputText: String): GeneratedResult {
        val result = GeneratedResult()

        if (inputText.length < 12 || inputText.length > 13) {
            result.errorMessage ="The correct number of digits is 13.";
            return result
        }

        var resultText = inputText
        if (resultText.length < 13) {
            resultText = EAN13CodeBuilder(resultText).fullCode
        }
        val barcodeFormat = BarcodeFormat.EAN_13
        return createBitmap(resultText, 80, barcodeFormat)
    }
    private fun createBitmap(inputText: String, height: Int, barcodeFormat: BarcodeFormat): GeneratedResult {
        val multiFormatWriter = MultiFormatWriter()
        val result = GeneratedResult()

        try {
            val bitMatrix: BitMatrix = multiFormatWriter.encode(
                inputText,
                barcodeFormat, 200, height
            )
            val barCodeEncoder = BarcodeEncoder()
            val bitmap: Bitmap = barCodeEncoder.createBitmap(bitMatrix)
            result.bitmap = bitmap
            result.qrCode = inputText
            result.barcodeFormat = barcodeFormat

        } catch (e: Exception) {
            e.printStackTrace()
            result.errorMessage = "Please check the number on the card."
        }
        return result
    }
}