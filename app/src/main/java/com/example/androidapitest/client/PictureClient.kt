package com.example.androidapitest.client

import com.example.androidapitest.model.Picture
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

interface PictureClient {

    @GET("/api/v1/images/")
    fun serch(@Query("query") query: String): Observable<List<Picture>>
}