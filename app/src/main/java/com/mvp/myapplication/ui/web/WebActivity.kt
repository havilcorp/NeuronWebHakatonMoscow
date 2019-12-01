package com.mvp.myapplication.ui.web

import android.Manifest
import android.os.Bundle
import butterknife.ButterKnife
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.mvp.myapplication.App
import com.mvp.myapplication.R
import com.mvp.myapplication.base.BaseActivity
import javax.inject.Inject


class WebActivity : BaseActivity(), WebContract.IView {

    override fun inject() {
        App[this].component.inject(this)
    }

    @Inject
    lateinit var presenter: WebPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        ButterKnife.bind(this)

        presenter.attachView(this)
        presenter.initializeView()
    }

    override fun initializeView() {

    }

}
