package com.mvp.myapplication.utils

import android.content.Context
import android.view.animation.Animation
import android.view.animation.AnimationUtils

class AnimUtils(val context: Context) {

    fun animListener(r: Int, list: () -> Unit): Animation {
        val anim = AnimationUtils.loadAnimation(context, r)
        anim.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                list()
            }
            override fun onAnimationStart(animation: Animation?) {}
        })
        return anim
    }

}
