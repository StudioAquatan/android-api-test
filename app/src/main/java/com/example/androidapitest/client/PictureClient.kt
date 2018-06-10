package com.example.androidapitest.client

import com.example.androidapitest.model.ImageList
import retrofit2.http.GET
import rx.Observable

interface PictureClient {

    @GET("/api/v1/images")
    fun search(): Observable<ImageList>
}