package com.mvp.myapplication.ui.main

import android.Manifest
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import butterknife.ButterKnife
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.mvp.myapplication.App
import com.mvp.myapplication.R
import com.mvp.myapplication.base.BaseActivity
import com.mvp.myapplication.data.models.ModelRect
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : BaseActivity(), MainContract.IView {

    override fun inject() {
        App[this].component.inject(this)
    }

    @Inject lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ButterKnife.bind(this)

        presenter.attachView(this)
        presenter.initializeView()
    }

    override fun initializeView() {

        val size = Point()
        windowManager.defaultDisplay.getSize(size)
        presenter.setScreenSize(size.x, size.y)

        findViewById<View>(R.id.actionCam).setOnClickListener {
            presenter.actionCreatePhoto()
        }

        findViewById<View>(R.id.actionGalery).setOnClickListener {
            presenter.actionGalery()
        }

        imageView.setOnTouchListener { v, event ->
            if(event.action == MotionEvent.ACTION_DOWN) {
                presenter.actionImage(event.x, event.y)
            }
            false
        }

        Dexter.withActivity(this)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    presenter.successCamPermission()
                }
                override fun onPermissionDenied(response: PermissionDeniedResponse) {}
                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {}
            }).check()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        presenter.onResultActivity(requestCode, resultCode, data, contentResolver)
    }

    override fun setImage(bitmap: Bitmap) {
        imageView.setImageBitmap(bitmap)
    }

    override fun drawRects(image: Bitmap, objects: ArrayList<ModelRect>, strokeWidth: Float, textSize: Float, textMargin: Float, rectRadius: Float) {
        val bitmap = Bitmap.createBitmap(
            image.width,
            image.height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)

        val paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = strokeWidth

        val paintText = Paint()
        paintText.style = Paint.Style.FILL
        paintText.textSize = textSize

        canvas.drawColor(Color.LTGRAY)
        canvas.drawBitmap(image, 0f, 0f, null)
        objects.forEach {
            paint.color = it.color
            paintText.color = it.color
            canvas.drawText(it.title, it.rect.left.toFloat() + textMargin, it.rect.top.toFloat() - textMargin, paintText)
            canvas.drawRoundRect(
                it.rect.left.toFloat(),
                it.rect.top.toFloat(),
                it.rect.right.toFloat(),
                it.rect.bottom.toFloat(),
                rectRadius, rectRadius, paint)
        }
        imageView.setImageBitmap(bitmap)
    }

}
