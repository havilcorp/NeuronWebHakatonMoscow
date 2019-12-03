package com.mvp.myapplication.ui.web

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
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
        web_actionArtVision.setOnClickListener {
            presenter.actionArtVision()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        presenter.onResultActivity(requestCode, resultCode, data)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun loadWebView(url: String) {
        val webViewClient: WebViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
            @TargetApi(Build.VERSION_CODES.N)
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                view.loadUrl(request.url.toString())
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                view!!.loadUrl("javascript: var css = '#menu,#seo-menu,.breadcrumbs,.smartbanner,.header,.catalog__control-section,.logo-line-wrapper,.wrapper-worm,.js-width-wrapper-breadcrumb,.js-products-catalog-title,.table__left-column,.multifilters-container,.js-multifilters-container,.unipop__container,.footer,.product-card__overlay-top-left,.product-card__overlay-top-right,.email-subscription,.x-footer,.installments,.social-proof,.product-comments,.product-sizes__subscribe-button,.checkout-price,.b-footer,.checkout__preview-container{  display:none !important}.products-list-item {max-width:315px;}.x-butto,.x-button_cart,.x-button_40 {background:#333;}', head = document.head || document.getElementsByTagName('head')[0], style = document.createElement('style'); head.appendChild(style); style.type = 'text/css'; if (style.styleSheet){ style.styleSheet.cssText = css; } else { style.appendChild(document.createTextNode(css)); }")
            }
        }
        webView.settings.javaScriptEnabled = true
        webView.settings.pluginState = WebSettings.PluginState.ON
        webView.webViewClient = webViewClient

        webView.loadUrl(url)

    }

    override fun reLoadUrl(url: String) {
        webView.loadUrl(url)
        webView.reload()
    }

    override fun showWebView() {
        webView.visibility = View.VISIBLE
    }

    override fun hideWebView() {
        webView.visibility = View.GONE
    }

}
