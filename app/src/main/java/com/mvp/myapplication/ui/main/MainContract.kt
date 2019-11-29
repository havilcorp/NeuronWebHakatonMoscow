package com.mvp.myapplication.ui.main

import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import com.mvp.myapplication.data.models.ModelRect
import com.mvp.myapplication.mvp.IMvpView

class MainContract {

    interface IView: IMvpView {
        fun initializeView()

        fun setImage(bitmap: Bitmap)
        fun drawRects(image: Bitmap, objects: ArrayList<ModelRect>, strokeWidth: Float, textSize: Float, textMargin: Float, rectRadius: Float)

    }

    interface IPresenter {
        fun initializeView()

        fun onResultActivity(requestCode: Int, resultCode: Int, data: Intent?, contentResolver: ContentResolver)

        fun setScreenSize(x: Int, y: Int)

        fun setImage(image: Bitmap)

        fun successCamPermission()
        fun actionGalery()
        fun actionCreatePhoto()

        fun actionImage(x: Float, y: Float)

    }

}