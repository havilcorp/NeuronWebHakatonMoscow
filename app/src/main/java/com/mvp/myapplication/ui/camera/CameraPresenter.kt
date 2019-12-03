package com.mvp.myapplication.ui.camera

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Rect
import android.os.Handler
import android.provider.MediaStore
import com.mvp.myapplication.base.BasePresenter
import com.mvp.myapplication.data.DataManager
import com.mvp.myapplication.data.IAppCallback
import com.mvp.myapplication.data.models.ModelRect
import com.mvp.myapplication.data.models.adapter.ModelItemString
import com.mvp.myapplication.data.models.api.Requests
import com.mvp.myapplication.utils.AttrList
import com.mvp.myapplication.utils.ColorUtils
import com.mvp.myapplication.utils.ImageUtils
import com.mvp.myapplication.utils.LangList
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton class CameraPresenter
@Inject constructor(private val dataManager: DataManager): BasePresenter<CameraContract.IView>(),
    CameraContract.IPresenter {

    private val compositeDisposable = CompositeDisposable()
    val CAMERA_ACTION_GALERY = 100
    var screenWidth = 0
    var screenHeight = 0
    var selectedImage: Bitmap? = null

    val listObjectsWithServer = ArrayList<ModelRect>()

    val REQUEST_CODE = 100

    override fun initializeView() {
        iMvpView?.initializeView()

    }

    override fun setScreenSize(width: Int, height: Int) {
        screenWidth = width
        screenHeight = height
    }

    override fun actionBack() {
        iMvpView?.backView()
    }

    override fun actionGalery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        iMvpView?.startActivityFResult(intent, CAMERA_ACTION_GALERY)
    }

    override fun actionPhoto() {
        iMvpView?.takePhoto()
        iMvpView?.showProgress()
    }

    override fun onResultActivity(requestCode: Int, resultCode: Int, data: Intent?, contentResolver: ContentResolver) {
        if(resultCode == Activity.RESULT_OK && requestCode == CAMERA_ACTION_GALERY && data != null){
            val imageUri = data.data
            val imageStream = contentResolver.openInputStream(imageUri!!)
            val imageBitmap = BitmapFactory.decodeStream(imageStream)
            selectedImage = ImageUtils.getResizedBitmap(imageBitmap, screenWidth, (screenWidth / imageBitmap.width.toFloat() * imageBitmap.height.toFloat()).toInt())
            selectedImage?.let {
                iMvpView?.setImage(it)
                Handler().postDelayed({
                    sendImageAndGetObjects(it)
                }, 500)
            }
        }
    }

    override fun setBitmap(imageBitmap: Bitmap) {
        iMvpView?.stopCamera()
        iMvpView?.hideProgress()
        selectedImage = ImageUtils.getResizedBitmap(imageBitmap, screenWidth, (screenWidth / imageBitmap.width.toFloat() * imageBitmap.height.toFloat()).toInt())
        selectedImage?.let {
            iMvpView?.setImage(it)
            Handler().postDelayed({
                sendImageAndGetObjects(it)
            }, 500)
        }
    }

    private fun sendImageAndGetObjects(image: Bitmap) {
        iMvpView?.showProgress()
        dataManager.getObjects(ImageUtils.encodeImageToBase64(image), object : IAppCallback<Requests.OBJECT_LOCALIZATION> {
            override fun onSuccess(response: Requests.OBJECT_LOCALIZATION) {
                iMvpView?.hideProgress()
                listObjectsWithServer.clear()
                val langList = LangList()
                response.responses.forEach {
                    it.localizedObjectAnnotations.forEach {
                        //if(langList.isset(it.name)) {
                        listObjectsWithServer.add(
                            ModelRect(
                                langList.getRus(it.name),
                                Rect(
                                    (it.boundingPoly.normalizedVertices[0].x * image.width).toInt(),
                                    (it.boundingPoly.normalizedVertices[1].y * image.height).toInt(),
                                    (it.boundingPoly.normalizedVertices[2].x * image.width).toInt(),
                                    (it.boundingPoly.normalizedVertices[3].y * image.height).toInt()
                                ),
                                Color.rgb(158,156,255)
                            )
                        )
                        //}
                    }
                }
                iMvpView?.drawRectsOnImage(image, listObjectsWithServer)
            }
        })
    }

    override fun actionPointImage(x: Float, y: Float) {
        selectedImage?.let { selectedImage ->
            val items = ArrayList<ModelItemString>()
            listObjectsWithServer.forEach { it.color = Color.rgb(158,156,255) }
            listObjectsWithServer.forEachIndexed { index, it ->
                if (it.rect.contains(x.toInt(), y.toInt())) {
                    it.color = Color.rgb(246,192,255)
                    items.add(ModelItemString(index, it.title))
                }
            }
            iMvpView?.drawRectsOnImage(selectedImage, listObjectsWithServer)
            if(items.size == 1) actionAlertDialogObjectItem(items[0].i)
            else if(items.size != 0) iMvpView?.showViewSelectObject(items)
        }
    }

    override fun actionAlertDialogObjectItem(item: Int) {
        selectedImage?.let { selectedImage ->
            val image = Bitmap.createBitmap(
                selectedImage,
                listObjectsWithServer[item].rect.left,
                listObjectsWithServer[item].rect.top,
                listObjectsWithServer[item].rect.right - listObjectsWithServer[item].rect.left,
                listObjectsWithServer[item].rect.bottom - listObjectsWithServer[item].rect.top
            )
            iMvpView?.showProgress()
            dataManager.getDetailObjects(ImageUtils.encodeImageToBase64(image), object : IAppCallback<Requests.OBJECT_DETECTION> {
                override fun onSuccess(response: Requests.OBJECT_DETECTION) {
                    iMvpView?.hideProgress()
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
                        var query = " "
                        items.forEach {
                            query += "${colors[0]} ${it.name}%20"
                        }
                        val intent = Intent()
                        intent.putExtra("query", "${colors[0]} ${items[0].name}")
                        iMvpView?.onActivityResultOk(intent)
                        iMvpView?.backView()
                    }
                }
            })
        }

    }

}