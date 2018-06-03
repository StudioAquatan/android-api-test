package com.example.androidapitest

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

// Don't use this version

open class Picture : RealmObject() {
    @PrimaryKey
    var id: Long = 0
    var date: Date = Date()
    var name: String = ""
    var picture: String = ""
}