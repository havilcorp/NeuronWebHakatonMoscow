package com.mvp.myapplication.mvp

interface IPresenter<V: IMvpView> {

    fun attachView(iMvpView: V)
    fun detachView()

}