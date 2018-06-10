package com.example.androidapitest

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.BaseAdapter
import android.widget.Toast
import com.bumptech.glide.Glide
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.yesButton
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val names: MutableList<String> = mutableListOf()
    private val dates: MutableList<Date> = mutableListOf()
    private val pictures: MutableList<String> = mutableListOf()
    private val REQUEST_CODE = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkpermission()

        /* インターネットから画像を取得した簡単な例 */
        for (i in 1..20) {
            names.add("test")
            "1999/10/22".toDate("yyyy/MM/dd")?.let { dates.add(it) }
            pictures.add("https://4.bp.blogspot.com/-2kOkRu5Z-Es/WtRzMCubiHI/AAAAAAABLko/qfsgPsAaaCs-J7B8_lLLEuWiAQro9uc7QCLcBGAs/s800/kumade_ebisu_daikoku_okame.png")
        }

        gridView.adapter = PicutureAdapter(R.layout.grid_items, names, dates, pictures)

        /* API更新のためのボタン(使うかは微妙？) */
        reload.setOnClickListener {

            alert("更新しました") {
                yesButton {  }
            }.show()

        }

        /* カメラ起動のためのボタン */
        camera.setOnClickListener {
            val intent = Intent(this, Camera::class.java)
            startActivity(intent)
        }
    }

    /* 端末のストレージに対する権限確認 */
    fun checkpermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        }else {
            requestLocationPermission()
        }
    }

    /* 権限要求 */
    fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE)
        }
        else {
            Toast.makeText(this, "Running this app needs your permission", Toast.LENGTH_SHORT).show()
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE)
        }
    }

    /* String型からDate型への変換 */
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
