package com.example.discountcardsapplication.utils

import android.graphics.Bitmap
import android.util.Log
import com.example.discountcardsapplication.models.GeneratedResult
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder

class CodeGenerator {

    fun generateQROrBarcodeImage(inputText: String, barcodeFormat: BarcodeFormat): GeneratedResult {
        return when (barcodeFormat) {
            BarcodeFormat.QR_CODE -> createBitmap(inputText, QR_CODE_IMAGE_HEIGHT, barcodeFormat)
            BarcodeFormat.EAN_13 -> generateEAN13Image(inputText)
            else -> createBitmap(inputText, BARCODE_IMAGE_HEIGHT, barcodeFormat)
        }
    }

    private fun generateEAN13Image(inputText: String): GeneratedResult {
        val result = GeneratedResult()

        if (inputText.length < REQUIRED_NUMBER_OF_DIGITS_TO_GENERATE ||
            inputText.length > EAN13_NUMBER_OF_DIGITS) {
            result.errorMessage = "The correct number of digits is 13."
            return result
        }

        var resultText = inputText
        if (resultText.length < EAN13_NUMBER_OF_DIGITS) {
            resultText = EAN13CodeBuilder(resultText).fullCode
        }
        val barcodeFormat = BarcodeFormat.EAN_13
        return createBitmap(resultText, BARCODE_IMAGE_HEIGHT, barcodeFormat)
    }
    private fun createBitmap(
        inputText: String,
        height: Int,
        barcodeFormat: BarcodeFormat
    ): GeneratedResult {
        val multiFormatWriter = MultiFormatWriter()
        val result = GeneratedResult()

        try {
            val bitMatrix: BitMatrix = multiFormatWriter.encode(
                inputText,
                barcodeFormat,
                CODE_IMAGE_WIDTH,
                height
            )
            val barCodeEncoder = BarcodeEncoder()
            val bitmap: Bitmap = barCodeEncoder.createBitmap(bitMatrix)
            result.bitmap = bitmap
            result.qrCode = inputText
            result.barcodeFormat = barcodeFormat
        } catch (e: IllegalArgumentException) {
            Log.e(LOG_TAG, e.toString())
            result.errorMessage = "Please check the number on the card."
        }
        return result
    }
    companion object {
        private const val QR_CODE_IMAGE_HEIGHT = 200
        private const val CODE_IMAGE_WIDTH = 200
        private const val BARCODE_IMAGE_HEIGHT = 80
        private const val EAN13_NUMBER_OF_DIGITS = 13
        private const val REQUIRED_NUMBER_OF_DIGITS_TO_GENERATE = 12
        private const val LOG_TAG = "CodeGenerator"
    }
}
