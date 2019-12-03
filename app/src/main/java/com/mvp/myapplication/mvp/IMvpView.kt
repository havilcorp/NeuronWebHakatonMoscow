package com.mvp.myapplication.mvp

import android.content.Intent

interface IMvpView {

    fun message(message : String?)
    fun showError(message: String?)

    fun showProgress()
    fun hideProgress()
    fun toBuffer(text: String)
    fun hideKayboard()
    fun toast(data: Any)
    fun startActivity(clazz: Class<*>, isFinish: Boolean, intent: Intent = Intent())
    fun startActivityFResult(intent: Intent, code: Int)
    fun startActivityFResult(clazz: Class<*>, code: Int)
    fun onActivityResultOk(intent: Intent)
    fun backView()
    fun setBackView()

}