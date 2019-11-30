package com.mvp.myapplication.data.api

import com.google.gson.JsonElement
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface IServierAPIInterface {

    @Headers(
        "Content-Type: application/json;charset=UTF-8",
        "Authorization: Bearer"
    )
    @POST("v1/images:annotate?key=AIzaSyBis9iaoitHh0-Y7sQNmsBCHBiKWXAwzus")
    fun getObjects(@Body body: JsonElement): Observable<Response<ResponseBody>>

}