package com.example.androidapitest

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Picture : RealmObject() {
    @PrimaryKey
    var id: Int = 0
    var date: Date = Date()
    var name: String = ""
    var picture: String = ""
}