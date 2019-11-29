package com.mvp.myapplication.data.network

import com.mvp.myapplication.data.models.api.Requests
import io.reactivex.Flowable
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response

/**
 * Created by havil on 19.03.2018.
 */

interface IDataBaseNetworkHelper {

    fun getObjects(imageBase64: String): Observable<Response<ResponseBody>>

    fun getListCity(): Observable<Requests.Tsrs>
    fun getTaxi(id: String): Observable<Requests.Tsrs>

    fun aaa(rest: String): Flowable<String>

}
