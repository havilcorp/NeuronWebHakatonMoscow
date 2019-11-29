package com.mvp.myapplication.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.os.Handler


class InternetUtils(private val activity: Activity){

    fun waitInternet(listener: (Boolean) -> Unit) {
        Handler().postDelayed({
            if(hasInternet()){
                listener(true)
            } else {
                listener(false)
                waitInternet(listener)
            }
        }, 5000)
    }

    fun waitInternetAll(listener: (Boolean) -> Unit) {
        Handler().postDelayed({
            listener(hasInternet())
            waitInternetAll(listener)
        }, 5000)
    }

    fun hasInternet(): Boolean{
        try {
            val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            if (wifiInfo != null && wifiInfo.isConnected)
            {
                //Log.d("TAG", "Вайфай включен")
                return true
            }
            wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            if (wifiInfo != null && wifiInfo.isConnected)
            {
                //Log.d("TAG", "Мобильный интернет включен")
                return true
            }
            wifiInfo = cm.getActiveNetworkInfo()
            if (wifiInfo != null && wifiInfo.isConnected)
            {
                //Log.d("TAG", "А тут вообще магический интернет включен")
                return true
            }
            return false
        } catch (e: Exception){
            return false
        }
    }

}
