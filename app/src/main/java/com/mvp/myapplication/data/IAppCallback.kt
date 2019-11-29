package com.mvp.myapplication.data

import android.util.Log
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response


interface IAppCallback <T> {

    fun onSuccess(response: T) {}
    fun onFailure(message: String?, e: Throwable) {
        e.stackTrace.forEach {
            Log.d("TAG", "ERROR: ${it.className} ${it.fileName} ${it.methodName} ${it.lineNumber}")
        }
    }
    fun onResponse(
        call: Call<ResponseBody?>?,
        response: Response<ResponseBody?>
    ) {
        Log.d("TAG", "onResponse")
    }

}