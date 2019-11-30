package com.mvp.myapplication.ui.logo

import com.mvp.myapplication.mvp.IMvpView

class LogoContract {

    interface IView: IMvpView {
        fun initializeView()

        fun checkPermission()
    }

    interface IPresenter {
        fun initializeView()

        fun successCamPermission()
    }

}