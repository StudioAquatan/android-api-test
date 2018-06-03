package com.example.androidapitest.client

import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

interface PictureClient {

    @GET("/api/v1/images/")
    fun serch(@Query("query") query: String): Observable<List</* Picture */>>
}