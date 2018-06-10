package com.example.androidapitest

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.androidapitest.model.Picture
import com.example.androidapitest.view.PictureView

class PicutureAdapter(private val context: Context) : BaseAdapter(){

    var pictures: List<Picture> = emptyList()

    override fun getCount(): Int = pictures.size

    override fun getItem(position: Int): Any? = pictures[position]

    override fun getItemId(position: Int): Long = 0

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View =
            ((convertView as? PictureView) ?: PictureView(context)).apply {
                setPicture(pictures[position])
            }
}