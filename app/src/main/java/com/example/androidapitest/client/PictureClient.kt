package com.example.androidapitest.client

import com.example.androidapitest.model.ImageList
import com.example.androidapitest.model.sendPicture
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import rx.Observable

interface PictureClient {

    @GET("/api/v1/images")
    fun search(): Observable<ImageList>

    @Multipart
    @POST
    fun postPicture(@Body picture: sendPicture): Observable<String>
}