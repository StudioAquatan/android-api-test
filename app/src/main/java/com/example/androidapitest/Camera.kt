package com.example.androidapitest

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.widget.Toast
import com.example.androidapitest.client.PictureClient
import com.example.androidapitest.model.sendPicture
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import com.trello.rxlifecycle.kotlin.bindToLifecycle
import kotlinx.android.synthetic.main.activity_camera.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class Camera : RxAppCompatActivity() {

    val CAMERA_REQUEST_CODE = 0
    var FIRST_TIME_ON = 0
    lateinit var imageFilePath: String
    lateinit var imageFileName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        val gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()

        val retrofit = Retrofit.Builder()
                .baseUrl("https://sample.drf.aquatan.studio")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()

        val pictureClient = retrofit.create(PictureClient::class.java)

        /* 画面遷移後カメラを実行するための条件文 */
        if (FIRST_TIME_ON == 0) {
            val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (callCameraIntent.resolveActivity(packageManager) != null) {
                val imageFile = createImageFile()
                val authorities = "$packageName.fileprovider"
                val imageUri = FileProvider.getUriForFile(this, authorities, imageFile)
                callCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startActivityForResult(callCameraIntent, CAMERA_REQUEST_CODE)
                FIRST_TIME_ON = 1
            }
        }

        val postTarget = sendPicture("test-from-app", createImageFile())

        /* APIのsend用のボタン */
        sendapiButton.setOnClickListener {
            addPicGallery(setImage())
            /* サーバへ送信 */
            pictureClient.postPicture(postTarget)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .bindToLifecycle(this)
                    .subscribe({
                    }, {
                        toast("エラー: $it")
                    })
            alert("送ったよ♡") {
                yesButton { }
            }.show()

        }

        /* カメラ起動のボタン */
        cameraButton.setOnClickListener {
            val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (callCameraIntent.resolveActivity(packageManager) != null) {
                val imageFile = createImageFile()
                val authorities = "$packageName.fileprovider"
                val imageUri = FileProvider.getUriForFile(this, authorities, imageFile)
                callCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startActivityForResult(callCameraIntent, CAMERA_REQUEST_CODE)
            }
        }
    }

    /* 写真撮影後の動作 */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    photoImageView.setImageBitmap(setImage())
                }
            }
            else -> {
                Toast.makeText(this, "Unrecognized request code", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /* 画像ファイルの作成 */
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_DCIM)
        val imageFile = File.createTempFile(imageFileName, ".jpg", storageDir)
        imageFilePath = imageFile.absolutePath
        return imageFile
    }

    /* ギャラリーへの追加と更新 */
    private fun addPicGallery(bitmap: Bitmap) {
        val albumdir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "100ANDRO")
        val picName: String = if (editpicName.text.isEmpty()) {
            "$imageFileName.jpg"
        } else {
            editpicName.text.toString() + ".jpg"
        }
        val file = File(albumdir, picName)
        val imageOut = FileOutputStream(file)

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, imageOut)
        imageOut.flush()
        imageOut.close()

        updateGallery("100ANDRO", albumdir, file)
    }

    /* ギャラリーの更新 */
    private fun updateGallery(albumName: String, albumDir: File, file: File) {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, imageFileName)
        values.put(MediaStore.Images.Media.DESCRIPTION, albumName)
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
        values.put(MediaStore.Images.ImageColumns.BUCKET_ID, file.toString().toLowerCase(Locale.JAPAN).hashCode())
        values.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, file.name.toLowerCase(Locale.JAPAN))
        values.put("_data", file.absolutePath)

        val cr = contentResolver
        cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val f = File(albumDir.absolutePath)
        val contentUri = Uri.fromFile(f)
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, contentUri)
        applicationContext.sendBroadcast(mediaScanIntent)
    }

    /* 画像ファイルのbitmap化 */
    private fun setImage(): Bitmap {
        val imageViewWidth = photoImageView.width
        val imageViewHeight = photoImageView.height

        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imageFilePath, bmOptions)
        val bitmapWidth = bmOptions.outWidth
        val bitmapHeight = bmOptions.outHeight

        val scanFactor = Math.min(bitmapWidth / imageViewWidth, bitmapHeight / imageViewHeight)

        bmOptions.inJustDecodeBounds = false
        bmOptions.inSampleSize = scanFactor

        var bitmap = BitmapFactory.decodeFile(imageFilePath, bmOptions)

        if (bitmap.width > bitmap.height) {
            bitmap = rotate90(bitmap)
        }

        return bitmap
    }

    /* 画像の90度回転 */
    private fun rotate90(bitmap: Bitmap): Bitmap {
        val newbitmap: Bitmap
        val rotateAngle = 90F
        val localMatrix = Matrix()
        val f1 = bitmap.width / 2F
        val f2 = bitmap.height / 2F
        localMatrix.postTranslate(-f1, -f2)
        localMatrix.postRotate(rotateAngle)
        localMatrix.postTranslate(f1, f2)
        newbitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, localMatrix, true)
        Canvas(newbitmap).drawBitmap(newbitmap, 0.0F, 0.0F, null)
        return newbitmap
    }
}
