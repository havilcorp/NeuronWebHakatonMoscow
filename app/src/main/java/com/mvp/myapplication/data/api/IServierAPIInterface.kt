package com.mvp.myapplication.data.api

import com.google.gson.JsonElement
import com.mvp.myapplication.data.models.api.Requests
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*


interface IServierAPIInterface {

    @GET("get_tsrs") fun getListTsrs(): Observable<Requests.Tsrs>

    @GET("get_tsr_org/{id}") fun getTsrOrg(@Path("id") id: String): Observable<Requests.Tsrs>

    //@FormUrlEncoded
    @Headers(
        "Content-Type: application/json;charset=UTF-8",
        "Authorization: Bearer"
    )
    @POST("v1/images:annotate?key=AIzaSyBis9iaoitHh0-Y7sQNmsBCHBiKWXAwzus")
    fun getObjects(@Body body: JsonElement): Observable<Response<ResponseBody>>

}