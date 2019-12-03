package com.mvp.myapplication.ui.logo

import android.os.Handler
import com.mvp.myapplication.base.BasePresenter
import com.mvp.myapplication.data.DataManager
import com.mvp.myapplication.ui.web.WebActivity
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton class LogoPresenter
@Inject constructor(private val dataManager: DataManager): BasePresenter<LogoContract.IView>(),
    LogoContract.IPresenter {

    private val compositeDisposable = CompositeDisposable()

    override fun initializeView() {
        iMvpView?.initializeView()

        Handler().postDelayed({
            iMvpView?.checkPermission()
        }, 1000)

    }

    override fun successCamPermission() {
        iMvpView?.startActivity(WebActivity::class.java, true)
    }

}