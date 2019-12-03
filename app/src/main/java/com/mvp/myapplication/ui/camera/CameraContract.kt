package com.mvp.myapplication.ui.camera

import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import com.mvp.myapplication.data.models.ModelRect
import com.mvp.myapplication.data.models.adapter.ModelItemString
import com.mvp.myapplication.mvp.IMvpView

class CameraContract {

    interface IView: IMvpView {
        fun initializeView()
        fun takePhoto()
        fun setImage(bitmap: Bitmap)

        fun drawRectsOnImage(image: Bitmap, listModelRects: ArrayList<ModelRect>)
        fun showViewSelectObject(listItem: ArrayList<ModelItemString>)

        fun startCamera()
        fun stopCamera()
    }

    interface IPresenter {
        fun initializeView()

        fun setScreenSize(width: Int, height: Int)

        fun actionBack()
        fun actionPhoto()
        fun actionGalery()

        fun onResultActivity(requestCode: Int, resultCode: Int, data: Intent?, contentResolver: ContentResolver)
        fun setBitmap(imageBitmap: Bitmap)

        fun actionPointImage(x: Float, y: Float)

        fun actionAlertDialogObjectItem(item: Int)


    }

}