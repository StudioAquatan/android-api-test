package com.example.androidapitest

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Picture
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import com.example.androidapitest.client.PictureClient
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.bumptech.glide.Glide
import com.example.androidapitest.view.PictureView
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.GridView
import android.widget.ImageButton
import android.widget.Toast
import com.example.androidapitest.client.PictureClient
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener {



    private val REQUEST_CODE = 1000
    private var gridAdapter: PicutureAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gridView: GridView = findViewById(R.id.gridView)
        val camera: ImageButton = findViewById(R.id.camera)
        val reload: ImageButton = findViewById(R.id.reload)

        checkpermission()

        gridAdapter = PicutureAdapter(applicationContext)
        gridView.adapter = gridAdapter

        val gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()

        val retrofit = Retrofit.Builder()
                .baseUrl("https://example.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()

        val pictureClient = retrofit.create(PictureClient::class.java)

        gridView.onItemClickListener = this

        reload.setOnClickListener {
            pictureClient.search()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        gridAdapter!!.pictures = it.results
                        gridAdapter!!.notifyDataSetChanged()
                    }, {
                        toast("エラー: $it")
                    })

            alert("更新しました") {
                yesButton { }
            }.show()
        }

        /* カメラ起動のためのボタン */
        camera.setOnClickListener {
            val intent = Intent(this, Camera::class.java)
            startActivity(intent)
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val intent = Intent(application, DisplayPictureActivity::class.java)
        intent.putExtra("IMAGEURI", gridAdapter!!.pictures[position].file)
        startActivity(intent)
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
}
