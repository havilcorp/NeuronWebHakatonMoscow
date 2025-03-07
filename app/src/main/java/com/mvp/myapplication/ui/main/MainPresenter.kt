package com.mvp.myapplication.ui.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Rect
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.mvp.myapplication.BuildConfig
import com.mvp.myapplication.base.BasePresenter
import com.mvp.myapplication.data.DataManager
import com.mvp.myapplication.data.IAppCallback
import com.mvp.myapplication.data.models.ModelRect
import com.mvp.myapplication.data.models.adapter.ModelItemString
import com.mvp.myapplication.data.models.api.Requests
import com.mvp.myapplication.ui.web.WebActivity
import com.mvp.myapplication.utils.*
import io.reactivex.disposables.CompositeDisposable
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList


@Singleton class MainPresenter
@Inject constructor(private val dataManager: DataManager): BasePresenter<MainContract.IView>(), MainContract.IPresenter {

    private val compositeDisposable = CompositeDisposable()
    private lateinit var internetUtils: InternetUtils

    val listRects = ArrayList<ModelRect>()

    var screenSizeX = 0f
    var screenSizeY = 0f
    var koeffX = 0f
    var koeffY = 0f
    var widthImage = 0
    var heightImage = 0

    val textSize = 35f
    val textMargin = 20f
    val strokeWidth = 5f
    val rectRadius = 10f

    var selectImage: Bitmap? = null

    val CAMERA_ACTION_GALERY = 1
    val CAMERA_ACTION_PHOTO = 2

    var cameraFilePath = ""

    override fun initializeView() {
        iMvpView?.initializeView()
    }

    @SuppressLint("SimpleDateFormat")
    private fun createImageFile() : File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_";
        val storageDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        val image = File.createTempFile(imageFileName,".jpg", storageDir)
        cameraFilePath = "file://" + image.absolutePath
        return image
    }

    override fun actionGalery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        iMvpView?.startActivityFResult(intent, CAMERA_ACTION_GALERY)
    }
    override fun actionCreatePhoto(context: Context) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", createImageFile()))
        iMvpView?.startActivityFResult(intent, CAMERA_ACTION_PHOTO)
    }

    override fun actionPhoto() {
        iMvpView?.hidePanel()
        iMvpView?.hideBackView()
        iMvpView?.showActionPhoto()
    }

    override fun actionSurfacePhoto() {
        iMvpView?.message("Ожидайте")
        iMvpView?.takePhoto()
    }

    override fun setBitmap(bitmap: Bitmap) {
        iMvpView?.hideSurfaceView()
        iMvpView?.hideActionPhoto()
        val imageBitmap = ImageUtils.getResizedBitmap(bitmap, screenSizeX.toInt(), (screenSizeX / bitmap.width.toFloat() * bitmap.height.toFloat()).toInt())!!
        iMvpView?.setImage(imageBitmap)
        Handler().postDelayed({
            sendImageToApiAndGetObjects(imageBitmap)
        }, 500)
    }

    override fun onResultActivity(requestCode: Int, resultCode: Int, data: Intent?, contentResolver: ContentResolver) {
        if(resultCode == Activity.RESULT_OK && requestCode == CAMERA_ACTION_PHOTO) {

            val imageUri = Uri.parse(cameraFilePath)
            val imageStream = contentResolver.openInputStream(imageUri!!)
            val imageBitmap = BitmapFactory.decodeStream(imageStream)

            iMvpView?.hidePanel()
            iMvpView?.hideSurfaceView()
            iMvpView?.hideBackView()
            iMvpView?.setImageUri90(imageUri)

            Handler().postDelayed({
                sendImageToApiAndGetObjects(imageBitmap)
            }, 500)

        }
        if(resultCode == Activity.RESULT_OK && requestCode == CAMERA_ACTION_GALERY && data != null){
            val imageUri = data.data
            val imageStream = contentResolver.openInputStream(imageUri!!)
            var imageBitmap = BitmapFactory.decodeStream(imageStream)

            imageBitmap = ImageUtils.getResizedBitmap(imageBitmap, screenSizeX.toInt(), (screenSizeX / imageBitmap.width.toFloat() * imageBitmap.height.toFloat()).toInt())

            iMvpView?.hidePanel()
            iMvpView?.hideSurfaceView()
            iMvpView?.hideBackView()
            iMvpView?.setImage(imageBitmap)

            Handler().postDelayed({
                sendImageToApiAndGetObjects(imageBitmap)
            }, 500)

        }
    }

    override fun actionBack() {
        iMvpView?.hideActionBack()
        iMvpView?.showPanel()
        iMvpView?.showSurfaceView()
        iMvpView?.showBackView()
    }

    override fun setScreenSize(x: Int, y: Int) {
        screenSizeX = x.toFloat()
        screenSizeY = y.toFloat()
    }

    override fun setNewSizeImage(width: Int, height: Int) {
        widthImage = width
        heightImage = height
    }

    private fun sendImageToApiAndGetObjects(image: Bitmap) {

        selectImage = image

        koeffX = screenSizeX / image.width
        koeffY = screenSizeY / image.height

        iMvpView?.showProgress()
        dataManager.getObjects(ImageUtils.encodeImageToBase64(image), object : IAppCallback<Requests.OBJECT_LOCALIZATION> {
            override fun onSuccess(response: Requests.OBJECT_LOCALIZATION) {
                iMvpView?.hideProgress()
                iMvpView?.showActionBack()
                listRects.clear()
                val langList = LangList()
                response.responses.forEach {
                    it.localizedObjectAnnotations.forEach {
                        //if(langList.isset(it.name)) {
                            listRects.add(
                                ModelRect(
                                    langList.getRus(it.name),
                                    Rect(
                                        (it.boundingPoly.normalizedVertices[0].x * image.width).toInt(),
                                        (it.boundingPoly.normalizedVertices[1].y * image.height).toInt(),
                                        (it.boundingPoly.normalizedVertices[2].x * image.width).toInt(),
                                        (it.boundingPoly.normalizedVertices[3].y * image.height).toInt()
                                    ),
                                    Color.rgb(150, 230, 255)
                                )
                            )
                        //}
                    }
                }
                iMvpView?.drawRects(image, listRects, strokeWidth, textSize, textMargin, rectRadius)
            }
        })
    }

    override fun actionPointImage(x: Float, y: Float) {

        selectImage?.let { selectImage ->

            val items = ArrayList<ModelItemString>()
            val newX = x// / koeffX
            val newY = y// / koeffY

            //Log.d("TAG", "screenSizeY ${screenSizeY} heightImage ${heightImage}")
            //Log.d("TAG", "koeffY ${koeffY}")
            //Log.d("TAG", "y ${y}")
            //Log.d("TAG", "newY ${newY}")

            listRects.forEach { it.color = Color.rgb(150, 255, 255) }
            listRects.forEachIndexed { index, it ->
                if (it.rect.contains(newX.toInt(), newY.toInt())) {
                    it.color = Color.rgb(0, 255, 0)
                    items.add(ModelItemString(index, it.title))
                }
            }
            iMvpView?.drawRects(
                selectImage,
                listRects,
                strokeWidth,
                textSize,
                textMargin,
                rectRadius
            )
            if(items.size != 0) iMvpView?.showViewSelectObject(items)

        }
    }

    override fun actionObjectItem(item: Int) {
        selectImage?.let { selectImage ->
            val image = Bitmap.createBitmap(
                selectImage,
                listRects[item].rect.left,
                listRects[item].rect.top,
                listRects[item].rect.right - listRects[item].rect.left,
                listRects[item].rect.bottom - listRects[item].rect.top
            )

            iMvpView?.hideActionBack()
            iMvpView?.showProgress()
            dataManager.getDetailObjects(ImageUtils.encodeImageToBase64(image), object : IAppCallback<Requests.OBJECT_DETECTION> {
                override fun onSuccess(response: Requests.OBJECT_DETECTION) {
                    iMvpView?.hideProgress()
                    iMvpView?.showActionBack()
                    val attrList = AttrList()
                    val items = ArrayList<ModelItemString>()
                    val colors = ArrayList<String>()
                    response.responses.forEach {
                        it.imagePropertiesAnnotation.dominantColors.colors.forEach {
                            colors.add(ColorUtils.getColorString(it.color.red, it.color.green, it.color.blue))
                        }
                        it.labelAnnotations.forEach {
                            //if(attrList.isset(it.description)) {
                                items.add(ModelItemString(0, attrList.getRus(it.description)))
                            //}
                        }
                    }
                    if(items.size != 0) {
                        //iMvpView?.showViewSelectObject(items)
                        var query = " "
                        items.forEach {
                            query += "${colors[0]} ${it.name}%20"
                        }
                        val intent = Intent()
                        intent.putExtra("query", query)
                        iMvpView?.startActivity(WebActivity::class.java, false, intent)
                    }
                }
            })
        }
    }

}