package com.example.discountcardsapplication.models

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

data class GeneratedResult (
    var errorMessage: String? =null,
    var bitmap: Bitmap? =null,
    var qrCode: String? =null
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readParcelable(Bitmap::class.java.classLoader),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(errorMessage)
        parcel.writeParcelable(bitmap, flags)
        parcel.writeString(qrCode)
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
