package com.mvp.myapplication.ui.main

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
        fun setImageUri90(uri: Uri)
        fun setImageUri(uri: Uri)
        fun setPicassoImage(uri: Uri)
        fun drawRects(image: Bitmap, objects: ArrayList<ModelRect>, strokeWidth: Float, textSize: Float, textMargin: Float, rectRadius: Float)

        fun showViewSelectObject(listItem: ArrayList<ModelItemString>)

        fun hidePanel()
        fun showPanel()

        fun showActionPhoto()
        fun hideActionPhoto()

        fun showSurfaceView()
        fun hideSurfaceView()

        fun showActionBack()
        fun hideActionBack()

        fun showBackView()
        fun hideBackView()

        fun takePhoto()

    }

    interface IPresenter {
        fun initializeView()

        fun onResultActivity(requestCode: Int, resultCode: Int, data: Intent?, contentResolver: ContentResolver)

        fun setScreenSize(x: Int, y: Int)
        fun setNewSizeImage(width: Int, height: Int)

        fun actionGalery()
        fun actionPhoto()
        fun actionSurfacePhoto()
        //fun loadedPhoto(uri: Uri, contentResolver: ContentResolver)
        fun setBitmap(bitmap: Bitmap)
        fun actionCreatePhoto(context: Context)

        fun actionPointImage(x: Float, y: Float)

        fun actionObjectItem(item: Int)

        fun actionBack()

    }

}