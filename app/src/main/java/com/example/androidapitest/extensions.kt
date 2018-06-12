package com.example.androidapitest

import android.content.Context
import android.support.annotation.IdRes
import android.view.View
import android.widget.Toast
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun <T : View> View.bindView(@IdRes id: Int): Lazy<T> = lazy {
    findViewById(id) as T
}

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT ){
    Toast.makeText(this,message,duration).show()
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