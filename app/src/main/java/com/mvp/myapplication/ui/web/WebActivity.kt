package com.mvp.myapplication.ui.web

import android.os.Bundle
import butterknife.ButterKnife
import com.mvp.myapplication.App
import com.mvp.myapplication.R
import com.mvp.myapplication.base.BaseActivity
import kotlinx.android.synthetic.main.activity_web.*
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

        val query = intent.getStringExtra("query")

        webView.loadUrl("https://www.lamoda.ru/catalogsearch/result/?q=$query")

    }

}
