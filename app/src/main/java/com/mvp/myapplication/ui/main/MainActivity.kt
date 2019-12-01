package com.mvp.myapplication.ui.main

import android.app.AlertDialog
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.SurfaceHolder
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
            //presenter.actionPhoto()
            presenter.actionCreatePhoto(this)
        }

        /*findViewById<View>(R.id.actionSurfacePhoto).setOnClickListener {
            presenter.actionSurfacePhoto()
        }*/

        findViewById<View>(R.id.actionGalery).setOnClickListener {
            presenter.actionGalery()
        }

        imageView.setOnTouchListener { v, event ->
            if(event.action == MotionEvent.ACTION_DOWN) {
                presenter.actionPointImage(event.x, event.y)
            }
            false
        }

        surfaceView.holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged( holder: SurfaceHolder?,  format: Int,  width: Int,  height: Int) {
                camera.setDisplayOrientation(90)
                //val parameters = camera.getParameters()
                //parameters.jpegQuality = 100
                //camera.parameters = parameters
                camera.setPreviewDisplay(holder)
                camera.startPreview()
            }
            override fun surfaceDestroyed(holder: SurfaceHolder?) {}
            override fun surfaceCreated(holder: SurfaceHolder?) {}
        })


        //Blurry.with(this).radius(10).sampling(2).onto(findViewById<ViewGroup>(R.id.blureView))

        //Blurry.with(this)
        //    .radius(25)
        //    .sampling(1)
        //    .color(Color.argb(50, 0, 0, 0))
        //    .async()
        //    .onto(findViewById<View>(R.id.blureView) as ViewGroup)

    }

    override fun onPause() {
        super.onPause()
        if(camera != null) {
            camera.stopPreview()
            camera.release()
        }
    }

    override fun onResume() {
        super.onResume()
        camera = android.hardware.Camera.open()
    }

    override fun showPanel() {
        findViewById<View>(R.id.blureView).visibility = View.VISIBLE
    }

    override fun hidePanel() {
        findViewById<View>(R.id.blureView).visibility = View.GONE
    }

    override fun showSurfaceView() {
        camera = android.hardware.Camera.open()
        findViewById<View>(R.id.surfaceView).visibility = View.VISIBLE
    }

    override fun hideSurfaceView() {
        findViewById<View>(R.id.surfaceView).visibility = View.GONE
    }

    override fun hideSurfaceCamera() {
        findViewById<View>(R.id.actionSurfacePhoto).visibility = View.GONE
    }

    override fun showSurfaceCamera() {
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
        /*camera.autoFocus { success, camera ->
            if(success) {
                camera.takePicture(null, null, null, object : android.hardware.Camera.PictureCallback {
                    override fun onPictureTaken(data: ByteArray?, camera: Camera?) {
                        try {
                            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                            val imageFileName = "ARTVISION_" + timeStamp + "_";
                            val storageDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
                            val image = File.createTempFile(imageFileName,".jpg", storageDir)
                            val file = File (image.absolutePath)
                            val os = FileOutputStream(file)
                            os.write(data)
                            os.close()
                            presenter.loadedPhoto(Uri.parse("file://" + image.absolutePath), contentResolver)
                        } catch (e: Exception) {
                            Log.d("TAG", e.message)
                        }
                    }
                })
            }
        }*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        presenter.onResultActivity(requestCode, resultCode, data, contentResolver)
    }

    override fun setImage(bitmap: Bitmap) {
        imageView.setImageBitmap(bitmap)
    }

    override fun setImageUri(uri: Uri) {
        imageView.setImageURI(uri)
        presenter.setNewSizeImage(imageView.width, imageView.height)
        imageView.animate().rotation(90F)
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
