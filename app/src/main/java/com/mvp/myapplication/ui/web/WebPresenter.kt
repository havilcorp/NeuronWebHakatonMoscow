package com.mvp.myapplication.ui.web

import android.os.Handler
import com.mvp.myapplication.base.BasePresenter
import com.mvp.myapplication.data.DataManager
import com.mvp.myapplication.ui.logo.LogoContract
import com.mvp.myapplication.ui.main.MainActivity
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton class WebPresenter
@Inject constructor(private val dataManager: DataManager): BasePresenter<WebContract.IView>(),
    WebContract.IPresenter {

    private val compositeDisposable = CompositeDisposable()

    override fun initializeView() {
        iMvpView?.initializeView()


    }

}