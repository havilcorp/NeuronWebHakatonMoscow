package com.mvp.myapplication.ui.main

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import com.mvp.myapplication.data.models.ModelRect
import com.mvp.myapplication.data.models.adapter.ModelItemString
import com.mvp.myapplication.mvp.IMvpView

class MainContract {

    interface IView: IMvpView {
        fun initializeView()

        fun setImage(bitmap: Bitmap)
        fun setImageUri(uri: Uri)
        fun setPicassoImage(uri: Uri)
        fun drawRects(image: Bitmap, objects: ArrayList<ModelRect>, strokeWidth: Float, textSize: Float, textMargin: Float, rectRadius: Float)

        fun showViewSelectObject(listItem: ArrayList<ModelItemString>)

    }

    interface IPresenter {
        fun initializeView()

        fun onResultActivity(requestCode: Int, resultCode: Int, data: Intent?, contentResolver: ContentResolver)

        fun setScreenSize(x: Int, y: Int)

        fun actionGalery()
        fun actionCreatePhoto(context: Context)

        fun actionPointImage(x: Float, y: Float)

        fun actionObjectItem(item: Int)

    }

}