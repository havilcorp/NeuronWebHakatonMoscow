package com.mvp.myapplication.ui.main

import android.R.attr
import android.app.AlertDialog
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ArrayAdapter
import butterknife.ButterKnife
import com.mvp.myapplication.App
import com.mvp.myapplication.R
import com.mvp.myapplication.base.BaseActivity
import com.mvp.myapplication.data.models.ModelRect
import com.mvp.myapplication.data.models.adapter.ModelItemString
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : BaseActivity(), MainContract.IView {

    var camera = android.hardware.Camera.open()

    override fun inject() {
        App[this].component.inject(this)
    }

    @Inject
    lateinit var presenter: MainPresenter

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

        findViewById<View>(R.id.actionBack).setOnClickListener {
            presenter.actionBack()
        }

        findViewById<View>(R.id.actionCam).setOnClickListener {
            presenter.actionPhoto()
            //presenter.actionCreatePhoto(this)
        }

        findViewById<View>(R.id.actionSurfacePhoto).setOnClickListener {
            presenter.actionSurfacePhoto()
        }

        findViewById<View>(R.id.actionGalery).setOnClickListener {
            presenter.actionGalery()
        }

        imageView.setOnTouchListener { v, event ->
            if(event.action == MotionEvent.ACTION_DOWN) {
                presenter.actionPointImage(event.x, event.y)
            }
            false
        }

    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        cameraView.stop()
        super.onStop()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        cameraView.start()

        cameraView.setOnPictureTakenListener { bitmap, rotationDegrees ->
            val matrix = Matrix()
            matrix.postRotate((-rotationDegrees).toFloat())
            runOnUiThread {
                presenter.setBitmap(Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true))
            }
        }
    }

    override fun showPanel() {
        findViewById<View>(R.id.blureView).visibility = View.VISIBLE
    }

    override fun hidePanel() {
        findViewById<View>(R.id.blureView).visibility = View.GONE
    }

    override fun showSurfaceView() {
        findViewById<View>(R.id.cameraView).visibility = View.VISIBLE
    }

    override fun hideSurfaceView() {
        findViewById<View>(R.id.cameraView).visibility = View.GONE
    }

    override fun hideActionPhoto() {
        findViewById<View>(R.id.actionSurfacePhoto).visibility = View.GONE
    }

    override fun showActionPhoto() {
        findViewById<View>(R.id.actionSurfacePhoto).visibility = View.VISIBLE
    }

    override fun showActionBack() {
        findViewById<View>(R.id.actionBack).visibility = View.VISIBLE
    }

    override fun hideActionBack() {
        findViewById<View>(R.id.actionBack).visibility = View.GONE
    }

    override fun showBackView() {
        findViewById<View>(R.id.backView).visibility = View.VISIBLE
    }

    override fun hideBackView() {
        findViewById<View>(R.id.backView).visibility = View.GONE
    }

    override fun takePhoto() {
        cameraView.takePicture()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        presenter.onResultActivity(requestCode, resultCode, data, contentResolver)
    }

    override fun setImage(bitmap: Bitmap) {
        imageView.setImageBitmap(bitmap)
    }

    override fun setImageUri90(uri: Uri) {
        imageView.setImageURI(uri)
        presenter.setNewSizeImage(imageView.width, imageView.height)
        imageView.animate().rotation(90F)
    }

    override fun setImageUri(uri: Uri) {
        imageView.setImageURI(uri)
        presenter.setNewSizeImage(imageView.width, imageView.height)
    }

    override fun setPicassoImage(uri: Uri) {
        Picasso.get().load(uri).into(imageView)
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

    override fun showViewSelectObject(listItem: ArrayList<ModelItemString>) {

        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.select_dialog_item)
        listItem.forEach {
            arrayAdapter.add(it.name)
        }

        AlertDialog.Builder(this)
            .setIcon(R.mipmap.ic_launcher)
            .setTitle("Выберите один")
            .setNegativeButton("Отмена") { dialog, which -> dialog.dismiss() }
            .setAdapter(arrayAdapter) { dialog, index ->
                presenter.actionObjectItem(listItem[index].i)
                dialog.dismiss()
            }
            .show()
    }

}
