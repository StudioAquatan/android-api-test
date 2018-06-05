package com.example.androidapitest.model

import android.os.Parcel
import android.os.Parcelable

data class Picture(val pk: Int,
                   val name: String,
                   val file: String,
                   val created_at: String) : Parcelable {
    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Picture> = object : Parcelable.Creator<Picture> {
            override fun createFromParcel(source: Parcel): Picture = source.run {
                Picture(readInt(), readString(), readString(), readString())
            }

            override fun newArray(size: Int): Array<Picture?> = arrayOfNulls(size)
        }
    }

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.run {
            writeInt(pk)
            writeString(name)
            writeString(file)
            writeString(created_at)
        }
    }
}