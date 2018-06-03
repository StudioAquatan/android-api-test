package com.example.androidapitest

import android.app.Application
import io.realm.Realm

// Don't use this version

class DatabaseSetting : Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }

}