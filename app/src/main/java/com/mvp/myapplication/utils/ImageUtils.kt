package com.mvp.myapplication.utils

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream

class ImageUtils() {

    companion object {

        fun encodeImageToBase64(image: Bitmap): String {
            val byteArrayOutputStream = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
            val encoded: String = Base64.encodeToString(byteArray, Base64.DEFAULT)
            return encoded
        }

    }

}