package com.mvp.myapplication.ui.web

import android.content.Intent
import android.os.Handler
import com.mvp.myapplication.base.BasePresenter
import com.mvp.myapplication.data.DataManager
import com.mvp.myapplication.ui.camera.CameraActivity
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton class WebPresenter
@Inject constructor(private val dataManager: DataManager): BasePresenter<WebContract.IView>(),
    WebContract.IPresenter {

    private val compositeDisposable = CompositeDisposable()
    val REQUEST_CODE = 100

    override fun initializeView() {
        iMvpView?.initializeView()

        iMvpView?.hideWebView()
        iMvpView?.showProgress()

        iMvpView?.loadWebView("https://m.lamoda.ru/c/4153/default-women/?is_new=1")

        Handler().postDelayed({
            iMvpView?.hideProgress()
            iMvpView?.showWebView()
        }, 3000)
    }

    override fun onResultActivity(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_CODE && data != null) {

            iMvpView?.hideWebView()
            iMvpView?.showProgress()

            iMvpView?.loadWebView("https://m.lamoda.ru/catalogsearch/result/?q=${data.getStringExtra("query")}")

            Handler().postDelayed({
                iMvpView?.hideProgress()
                iMvpView?.showWebView()
            }, 3000)
        }
    }

    override fun actionArtVision() {
        iMvpView?.startActivityFResult(CameraActivity::class.java, REQUEST_CODE)
    }

}