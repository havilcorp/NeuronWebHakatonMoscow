package com.mvp.myapplication.ui.main

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Rect
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import com.mvp.myapplication.base.BasePresenter
import com.mvp.myapplication.data.DataManager
import com.mvp.myapplication.data.IAppCallback
import com.mvp.myapplication.data.models.ModelRect
import com.mvp.myapplication.data.models.api.Requests
import com.mvp.myapplication.utils.InternetUtils
import io.reactivex.disposables.CompositeDisposable
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton class MainPresenter
@Inject constructor(private val dataManager: DataManager): BasePresenter<MainContract.IView>(), MainContract.IPresenter {

    private val compositeDisposable = CompositeDisposable()
    private lateinit var internetUtils: InternetUtils

    val listRects = ArrayList<ModelRect>()

    var screenSizeX = 0f
    var screenSizeY = 0f
    var koeffX = 0f
    var koeffY = 0f

    lateinit var selectImage: Bitmap
    var textSize = 0f
    var textMargin = 0f
    var strokeWidth = 0f
    var rectRadius = 0f

    val CAMERA_ACTION_GALERY = 1
    val CAMERA_ACTION_PHOTO = 2

    override fun initializeView() {
        iMvpView?.initializeView()
    }

    override fun onResultActivity(requestCode: Int, resultCode: Int, data: Intent?, contentResolver: ContentResolver) {
        if(resultCode == Activity.RESULT_OK && requestCode == CAMERA_ACTION_PHOTO && data != null) {
            val imageBitmap = data.extras?.get("data") as Bitmap

            textSize = 8f
            textMargin = 5f
            strokeWidth = 1f
            rectRadius = 5f

            setImage(imageBitmap)
        }
        if(resultCode == Activity.RESULT_OK && requestCode == CAMERA_ACTION_GALERY && data != null){
            val imageUri = data.data
            val imageStream = contentResolver.openInputStream(imageUri!!)
            val imageBitmap = BitmapFactory.decodeStream(imageStream)

            textSize = 100f
            textMargin = 50f
            strokeWidth = 20f
            rectRadius = 20f

            setImage(imageBitmap)
        }
    }

    override fun setScreenSize(x: Int, y: Int) {
        screenSizeX = x.toFloat()
        screenSizeY = y.toFloat()
    }

    override fun successCamPermission() {

    }

    override fun actionGalery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        iMvpView?.startActivityFResult(intent, CAMERA_ACTION_GALERY)
    }
    override fun actionCreatePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        iMvpView?.startActivityFResult(intent, CAMERA_ACTION_PHOTO)
    }

    override fun setImage(image: Bitmap) {

        selectImage = image

        iMvpView?.showProgress()

        Log.d("TAG", "$screenSizeX ${image.width} $screenSizeY ${image.height}")

        koeffX = screenSizeX / image.width
        koeffY = screenSizeY / image.height

        Log.d("TAG", "koeffX $koeffX koeffY $koeffY")

        val byteArrayOutputStream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
        val encoded: String = Base64.encodeToString(byteArray, Base64.DEFAULT)

        dataManager.getObjects(encoded, object : IAppCallback<Requests.OBJECT_LOCALIZATION> {
            override fun onSuccess(response: Requests.OBJECT_LOCALIZATION) {
                listRects.clear()
                response.responses.forEach {
                    it.localizedObjectAnnotations.forEach {
                        listRects.add(
                            ModelRect(
                                it.name,
                                Rect(
                                    (it.boundingPoly.normalizedVertices[0].x * image.width).toInt(),
                                    (it.boundingPoly.normalizedVertices[1].y * image.height).toInt(),
                                    (it.boundingPoly.normalizedVertices[2].x * image.width).toInt(),
                                    (it.boundingPoly.normalizedVertices[3].y * image.height).toInt()
                                ),
                                Color.rgb(150, 230, 255)
                            )
                        )
                    }
                }
                iMvpView?.drawRects(image, listRects, strokeWidth, textSize, textMargin, rectRadius)
                iMvpView?.hideProgress()
            }
        })
    }

    override fun actionImage(x: Float, y: Float) {
        var breakForeach = false
        val newX = x / koeffX
        val newY = y / koeffY
        //Log.d("TAG", "koeffX $koeffX koeffY $koeffY x = $newX y = $newY")
        listRects.forEach { it.color = Color.rgb(150, 255, 255) }
        listRects.forEach {
            if(it.rect.contains(newX.toInt(), newY.toInt()) && !breakForeach) {
                it.color = Color.rgb(0, 255, 0)
                //breakForeach = true
            }
        }
        iMvpView?.drawRects(selectImage, listRects, strokeWidth, textSize, textMargin, rectRadius)
    }

}