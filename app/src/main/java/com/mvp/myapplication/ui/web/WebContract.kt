package com.mvp.myapplication.ui.web

import android.content.Intent
import com.mvp.myapplication.mvp.IMvpView

class WebContract {

    interface IView: IMvpView {
        fun initializeView()

        fun loadWebView(url: String)
        fun reLoadUrl(url: String)

        fun showWebView()
        fun hideWebView()
    }

    interface IPresenter {
        fun initializeView()

        fun onResultActivity(requestCode: Int, resultCode: Int, data: Intent?)

        fun actionArtVision()
    }

}