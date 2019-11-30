package com.mvp.myapplication.data.network

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response

/**
 * Created by havil on 19.03.2018.
 */

interface IDataBaseNetworkHelper {

    fun getObjects(imageBase64: String): Observable<Response<ResponseBody>>
    fun getDetailObjects(imageBase64: String): Observable<Response<ResponseBody>>

}
