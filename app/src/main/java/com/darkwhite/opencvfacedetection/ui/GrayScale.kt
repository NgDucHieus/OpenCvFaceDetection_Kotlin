package com.darkwhite.opencvfacedetection.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import java.io.InputStream

fun convertDrawableToGrayscaleOpenCV(context: Context, resourceId: Int): Bitmap? {
    return try {
        // Load image from resources as Bitmap
        val originalBitmap = BitmapFactory.decodeResource(context.resources, resourceId)

        // Convert Bitmap to OpenCV Mat
        val mat = Mat()
        Utils.bitmapToMat(originalBitmap, mat)

        // Convert to Grayscale
        val grayMat = Mat()
        Imgproc.cvtColor(mat, grayMat, Imgproc.COLOR_BGR2GRAY)

        // Convert Mat back to Bitmap
        val grayBitmap = Bitmap.createBitmap(grayMat.cols(), grayMat.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(grayMat, grayBitmap)

        grayBitmap
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}