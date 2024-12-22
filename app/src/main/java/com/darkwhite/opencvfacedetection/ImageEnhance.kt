package com.darkwhite.opencvfacedetection



import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp

fun histogramEqualization(bitmap: Bitmap): Bitmap {
    val width = bitmap.width
    val height = bitmap.height
    val resultBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)

    // Convert the image to grayscale
    val grayscale = IntArray(width * height)
    var index = 0
    for (y in 0 until height) {
        for (x in 0 until width) {
            val pixel = bitmap.getPixel(x, y)
            val r = (pixel shr 16 and 0xFF)
            val g = (pixel shr 8 and 0xFF)
            val b = (pixel and 0xFF)
            grayscale[index++] = (0.3 * r + 0.59 * g + 0.11 * b).toInt()
        }
    }

    // Compute the histogram
    val histogram = IntArray(256)
    for (gray in grayscale) {
        histogram[gray]++
    }

    // Compute the cumulative distribution function (CDF)
    val cdf = IntArray(256)
    cdf[0] = histogram[0]
    for (i in 1 until histogram.size) {
        cdf[i] = cdf[i - 1] + histogram[i]
    }

    // Normalize the CDF
    val cdfMin = cdf.first { it > 0 }
    val totalPixels = width * height
    val equalized = IntArray(256)
    for (i in cdf.indices) {
        equalized[i] = ((cdf[i] - cdfMin) * 255 / (totalPixels - cdfMin)).coerceIn(0, 255)
    }

    // Map the original grayscale values to the equalized values
    index = 0
    for (y in 0 until height) {
        for (x in 0 until width) {
            val gray = grayscale[index++]
            val newGray = equalized[gray]
            val newPixel = (0xFF shl 24) or (newGray shl 16) or (newGray shl 8) or newGray
            resultBitmap.setPixel(x, y, newPixel)
        }
    }

    return resultBitmap
}

@Composable
fun HistogramEqualizationScreen(bitmap: Bitmap) {
    val equalizedBitmap = remember { histogramEqualization(bitmap) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Original Image")
        Image(bitmap = bitmap.asImageBitmap(), contentDescription = "Original Image", modifier = Modifier.size(200.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Text("Equalized Image")
        Image(bitmap = equalizedBitmap.asImageBitmap(), contentDescription = "Equalized Image", modifier = Modifier.size(200.dp))
    }
}
