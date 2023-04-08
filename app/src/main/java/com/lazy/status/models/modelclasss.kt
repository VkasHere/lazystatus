package com.lazy.status.models

import android.os.Parcel
import android.os.Parcelable

data class modelclasss(
    val fileName: String?,
    val fileUri: String?
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(fileName)
        parcel.writeString(fileUri)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<modelclasss> {
        override fun createFromParcel(parcel: Parcel): modelclasss {
            return modelclasss(parcel)
        }

        override fun newArray(size: Int): Array<modelclasss?> {
            return arrayOfNulls(size)
        }
    }
}