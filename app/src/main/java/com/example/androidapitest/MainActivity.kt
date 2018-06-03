package com.example.androidapitest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        realm = Realm.getDefaultInstance()
        val pictures = realm.where<Picture>().findAll()
        listView.adapter = PicutureAdapter(pictures)
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}
