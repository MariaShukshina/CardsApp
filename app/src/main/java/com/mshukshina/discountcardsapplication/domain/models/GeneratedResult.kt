package com.mshukshina.discountcardsapplication.domain.models

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import com.google.zxing.BarcodeFormat

data class GeneratedResult(
    var errorMessage: String? = null,
    var bitmap: Bitmap? = null,
    var qrCode: String? = null,
    var barcodeFormat: BarcodeFormat? = null,
    var isSelected: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readParcelable(Bitmap::class.java.classLoader),
        parcel.readString(),
        parcel.readSerializable() as BarcodeFormat?,
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(errorMessage)
        parcel.writeParcelable(bitmap, flags)
        parcel.writeString(qrCode)
        parcel.writeSerializable(barcodeFormat)
        parcel.writeInt(isSelected)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GeneratedResult> {
        override fun createFromParcel(parcel: Parcel): GeneratedResult {
            return GeneratedResult(parcel)
        }

        override fun newArray(size: Int): Array<GeneratedResult?> {
            return arrayOfNulls(size)
        }
    }
}
