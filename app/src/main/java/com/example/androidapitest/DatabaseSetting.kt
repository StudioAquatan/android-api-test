package com.example.androidapitest

import android.app.Application
import io.realm.Realm

class DatabaseSetting : Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }

}