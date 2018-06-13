package com.example.androidapitest

import android.net.Uri
import android.os.Bundle
import com.bumptech.glide.Glide
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import kotlinx.android.synthetic.main.activity_display_picture.*

class DisplayPictureActivity : RxAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_picture)

        val intent = intent
        val imageUrl = intent.getStringExtra("IMAGEURI")

        Glide.with(displayview).load(Uri.parse(imageUrl)).into(displayview)
    }
}
