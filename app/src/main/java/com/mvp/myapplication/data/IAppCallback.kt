package com.mvp.myapplication.data

import android.util.Log


interface IAppCallback <T> {

    fun onSuccess(response: T) {}
    fun onFailure(message: String?, e: Throwable) {
        e.stackTrace.forEach {
            Log.d("TAG", "ERROR: $message ${it.className} ${it.fileName} ${it.methodName} ${it.lineNumber}")
        }
    }

}