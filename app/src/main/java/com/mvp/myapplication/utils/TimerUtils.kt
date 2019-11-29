package com.mvp.myapplication.utils

import android.os.Handler

class TimerUtils(val isNext: () -> Unit) {

    var h: Handler? = null

    var hc: Handler.Callback = Handler.Callback {
        isNext()
        false
    }

    init {
        h = Handler(hc)
    }

    fun stop(){
        h?.removeMessages(1)
    }

    fun start(time: Long){
        //Log.d("TAG", "RunTimer $time")
        stop()
        h?.sendEmptyMessageDelayed(1, time)
    }

}
