package com.example.androidapitest.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.androidapitest.R
import com.example.androidapitest.bindView
import com.example.androidapitest.model.Picture

class PictureView : FrameLayout {
    constructor(context: Context?) : super(context)

    constructor(context: Context?,
                attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?,
                attrs: AttributeSet?,
                defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(context: Context?,
                attrs: AttributeSet?,
                defStyleAttr: Int,
                defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    val pictureImageView: ImageView by bindView(R.id.imageView)
    val nameTextView: TextView by bindView(R.id.name)
    val dateTextView: TextView by bindView(R.id.date)


    init {
        LayoutInflater.from(context).inflate(R.layout.grid_items, this)
    }

    fun setPicture(picture: Picture){
        nameTextView.text=picture.name
        dateTextView.text=picture.created_at
        Glide.with(context).load(picture.file).into(pictureImageView)
    }
}