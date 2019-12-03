package com.mvp.myapplication.ui.camera

import android.app.AlertDialog
import android.content.Intent
import android.graphics.*
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
import kotlinx.android.synthetic.main.activity_camera.*
import javax.inject.Inject


class CameraActivity : BaseActivity(), CameraContract.IView {

    override fun inject() {
        App[this].component.inject(this)
    }

    @Inject
    lateinit var presenter: CameraPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        ButterKnife.bind(this)

        presenter.attachView(this)
        presenter.initializeView()
    }

    override fun initializeView() {
        val size = Point()
        windowManager.defaultDisplay.getSize(size)
        presenter.setScreenSize(size.x, size.y)

        camera_actionBack.setOnClickListener { presenter.actionBack() }
        camera_actionPhoto.setOnClickListener { presenter.actionPhoto() }
        camera_actionGalery.setOnClickListener { presenter.actionGalery() }
        camera_ImageView.setOnTouchListener { v, event ->
            if(event.action == MotionEvent.ACTION_DOWN) { presenter.actionPointImage(event.x, event.y) }
            false
        }
    }

    override fun onStop() {
        stopCamera()
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        startCamera()
    }

    override fun startCamera() {
        camera_cameraView.start()
        camera_cameraView.setOnPictureTakenListener { bitmap, rotationDegrees ->
            val matrix = Matrix()
            matrix.postRotate((-rotationDegrees).toFloat())
            runOnUiThread {
                presenter.setBitmap(Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true))
            }
        }
    }

    override fun stopCamera() {
        camera_cameraView.stop()
        camera_cameraView.removeCallbacks {}
    }

    override fun takePhoto() {
        camera_cameraView.takePicture()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        presenter.onResultActivity(requestCode, resultCode, data, contentResolver)
    }

    override fun setImage(bitmap: Bitmap) {
        camera_ImageView.setImageBitmap(bitmap)
        camera_ImageView.visibility = View.VISIBLE
    }

    override fun showViewSelectObject(listItem: ArrayList<ModelItemString>) {
        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.select_dialog_item)
        listItem.forEach { arrayAdapter.add(it.name) }
        AlertDialog.Builder(this)
            .setIcon(R.mipmap.ic_launcher)
            .setTitle("Выберите один")
            .setNegativeButton("Отмена") { dialog, which -> dialog.dismiss() }
            .setAdapter(arrayAdapter) { dialog, index ->
                presenter.actionAlertDialogObjectItem(listItem[index].i)
                dialog.dismiss()
            }
            .show()
    }

    override fun drawRectsOnImage(image: Bitmap, listModelRects: ArrayList<ModelRect>) {
        val bitmap = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f
        val paintText = Paint()
        paintText.style = Paint.Style.FILL
        paintText.textSize = 28f
        paintText.color = Color.rgb(140,234,255)
        canvas.drawColor(Color.LTGRAY)
        canvas.drawBitmap(image, 0f, 0f, null)
        listModelRects.forEach {
            paint.color = it.color
            //canvas.drawCircle(it.rect.centerX().toFloat(), it.rect.centerY().toFloat(), 10f, paint)
            //canvas.drawLine(
            //    it.rect.centerX().toFloat() - 5f,
            //    it.rect.centerY().toFloat() - 5f,
            //    it.rect.centerX().toFloat() - 150f,
            //    it.rect.centerY().toFloat() - 150f,
            //    paint)
            //canvas.drawText(it.title, it.rect.centerX().toFloat() - 160f, it.rect.centerY().toFloat() - 160f, paintText)
            canvas.drawText(it.title, it.rect.left.toFloat() + 15f, it.rect.top.toFloat() + 30f, paintText)
            canvas.drawRoundRect(
                it.rect.left.toFloat(),
                it.rect.top.toFloat(),
                it.rect.right.toFloat(),
                it.rect.bottom.toFloat(),
                10f, 10f, paint)
        }
        setImage(bitmap)
    }




}
