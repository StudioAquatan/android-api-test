package com.example.androidapitest.model

import android.os.Parcel
import android.os.Parcelable

data class ImageList(val count: Int,
                     val next: Int,
                     val previous: Int,
                     val results: ArrayList<Picture>) : Parcelable {
    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<ImageList> = object : Parcelable.Creator<ImageList> {
            override fun createFromParcel(source: Parcel): ImageList = source.run {
                ImageList(readInt(), readInt(), readInt(), readList(ArrayList<Picture>(), Picture::class.java.classLoader) as ArrayList<Picture>)

            }

            override fun newArray(size: Int): Array<ImageList?> = arrayOfNulls(size)
        }
    }

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.run {
            writeInt(count)
            writeInt(next)
            writeInt(previous)
//            writeList(results)
        }
    }
}