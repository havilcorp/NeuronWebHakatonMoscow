package com.mvp.myapplication.ui.web

import com.mvp.myapplication.mvp.IMvpView

class WebContract {

    interface IView: IMvpView {
        fun initializeView()
    }

    interface IPresenter {
        fun initializeView()
    }

}