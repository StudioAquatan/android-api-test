package com.example.androidapitest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.GridView
import android.widget.ImageButton
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
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gridView: GridView = findViewById(R.id.gridView)
        val camera: ImageButton = findViewById(R.id.camera)
        val reload: ImageButton = findViewById(R.id.reload)

        val gridAdapter = PicutureAdapter(applicationContext)
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

        reload.setOnClickListener {
            pictureClient.search()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        gridAdapter.pictures = it.results
                        gridAdapter.notifyDataSetChanged()
                    }, {
                        toast("エラー: $it")
                    })

            alert("更新しました") {
                yesButton { }
            }.show()
        }

        camera.setOnClickListener {

        }
    }

    fun String.toDate(pattern: String = "yyyy/MM/dd HH:mm"): Date? {
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
