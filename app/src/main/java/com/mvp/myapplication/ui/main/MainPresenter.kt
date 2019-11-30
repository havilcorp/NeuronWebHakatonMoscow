package com.mvp.myapplication.ui.main

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Rect
import android.os.Handler
import android.provider.MediaStore
import android.util.Base64
import com.mvp.myapplication.base.BasePresenter
import com.mvp.myapplication.data.DataManager
import com.mvp.myapplication.data.IAppCallback
import com.mvp.myapplication.data.models.ModelRect
import com.mvp.myapplication.data.models.adapter.ModelItemString
import com.mvp.myapplication.data.models.api.Requests
import com.mvp.myapplication.utils.ImageUtils
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

            iMvpView?.setImage(imageBitmap)

            Handler().postDelayed({
                sendImageToApiAndGetObjects(imageBitmap)
            }, 1000)

        }
        if(resultCode == Activity.RESULT_OK && requestCode == CAMERA_ACTION_GALERY && data != null){
            val imageUri = data.data
            val imageStream = contentResolver.openInputStream(imageUri!!)
            val imageBitmap = BitmapFactory.decodeStream(imageStream)

            textSize = 100f
            textMargin = 50f
            strokeWidth = 20f
            rectRadius = 20f

            iMvpView?.setImage(imageBitmap)

            Handler().postDelayed({
                sendImageToApiAndGetObjects(imageBitmap)
            }, 1000)

        }
    }

    override fun setScreenSize(x: Int, y: Int) {
        screenSizeX = x.toFloat()
        screenSizeY = y.toFloat()
    }

    override fun actionGalery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        iMvpView?.startActivityFResult(intent, CAMERA_ACTION_GALERY)
    }
    override fun actionCreatePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        iMvpView?.startActivityFResult(intent, CAMERA_ACTION_PHOTO)
    }

    private fun sendImageToApiAndGetObjects(image: Bitmap) {

        selectImage = image

        koeffX = screenSizeX / image.width
        koeffY = screenSizeY / image.height

        iMvpView?.showProgress()
        dataManager.getObjects(ImageUtils.encodeImageToBase64(image), object : IAppCallback<Requests.OBJECT_LOCALIZATION> {
            override fun onSuccess(response: Requests.OBJECT_LOCALIZATION) {
                iMvpView?.hideProgress()
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
            }
        })
    }

    override fun actionPointImage(x: Float, y: Float) {
        val items = ArrayList<ModelItemString>()
        val newX = x / koeffX
        val newY = y / koeffY
        //Log.d("TAG", "koeffX $koeffX koeffY $koeffY x = $newX y = $newY")
        listRects.forEach { it.color = Color.rgb(150, 255, 255) }
        listRects.forEachIndexed { index, it ->
            if(it.rect.contains(newX.toInt(), newY.toInt())) {
                it.color = Color.rgb(0, 255, 0)
                items.add(ModelItemString(index, it.title))
            }
        }
        iMvpView?.drawRects(selectImage, listRects, strokeWidth, textSize, textMargin, rectRadius)
        iMvpView?.showViewSelectObject(items)
    }

    override fun actionObjectItem(item: Int) {

        val image = Bitmap.createBitmap(
            selectImage,
            listRects[item].rect.left,
            listRects[item].rect.top,
            listRects[item].rect.right - listRects[item].rect.left,
            listRects[item].rect.bottom - listRects[item].rect.top
        )

        iMvpView?.showProgress()
        dataManager.getDetailObjects(ImageUtils.encodeImageToBase64(image), object : IAppCallback<Requests.OBJECT_DETECTION> {
            override fun onSuccess(response: Requests.OBJECT_DETECTION) {
                iMvpView?.hideProgress()
                val items = ArrayList<ModelItemString>()
                response.responses.forEach {
                    it.labelAnnotations.forEach {
                        items.add(ModelItemString(0, it.description))
                    }
                }
                iMvpView?.showViewSelectObject(items)
            }
        })
    }

}