package com.example.androidapitest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.BaseAdapter
import com.bumptech.glide.Glide
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val names: MutableList<String> = mutableListOf()
    private val dates: MutableList<Date> = mutableListOf()
    private val pictures: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        for (i in 1..20) {
            names.add("test")
            "1999/10/22".toDate("yyyy/MM/dd")?.let { dates.add(it) }
            pictures.add("https://4.bp.blogspot.com/-2kOkRu5Z-Es/WtRzMCubiHI/AAAAAAABLko/qfsgPsAaaCs-J7B8_lLLEuWiAQro9uc7QCLcBGAs/s800/kumade_ebisu_daikoku_okame.png")
        }

        gridView.adapter = PicutureAdapter(R.layout.grid_items, names, dates, pictures)

        reload.setOnClickListener {

            alert("更新しました") {
                yesButton {  }
            }.show()

        }

        camera.setOnClickListener {

        }
    }

    fun String.toDate(pattern: String = "yyyy/MM/dd HH:mm") : Date? {
        val sdFormat = try {
            SimpleDateFormat(pattern)
        } catch (e: IllegalArgumentException) {
            null
        }
        val date = sdFormat?.let {
            try {
                it.parse(this)
            } catch (e: ParseException) {
                null
            }
        }

        return date
    }
}
