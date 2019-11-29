package com.mvp.myapplication.base

import com.mvp.myapplication.mvp.IMvpView
import com.mvp.myapplication.mvp.IPresenter
import javax.inject.Inject

open class BasePresenter <T : IMvpView> @Inject constructor() : IPresenter<T> {

    var iMvpView: T? = null
        private set

    val isViewAttached : Boolean
        get() = iMvpView != null

    override fun attachView(iMvpView : T){
        this.iMvpView = iMvpView
    }

    override fun detachView() {
        iMvpView = null
    }

    fun checkViewAttachrd(){
        if(!isViewAttached) throw MvpViewNotAttachedException()
    }

    class MvpViewNotAttachedException :
            RuntimeException("Please call Presenter.attachView(MvpView) before" + " requesting data to the Presenter")

}