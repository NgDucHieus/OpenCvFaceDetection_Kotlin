package com.darkwhite.opencvfacedetection

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.media.Image
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.content.MediaType.Companion.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.ColorMatrixColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.darkwhite.opencvfacedetection.ui.MainContent
import com.darkwhite.opencvfacedetection.ui.convertDrawableToGrayscaleOpenCV
import com.darkwhite.opencvfacedetection.ui.theme.OpenCVFaceDetectionTheme
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityCompat.requestPermissions(
            this,
            REQUIRED_PERMISSIONS,
            0
        )

        if (OpenCVLoader.initLocal()) {
            Log.d(TAG, "onCreate: OpenCVLoader LOADED SUCCESSFULLY")
        } else {
            Log.e(TAG, "onCreate: OpenCVLoader LOADING FAILED")
        }

        val faceDetector = MyUtils.initFaceDetector(this)

        setContent {
            OpenCVFaceDetectionTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (faceDetector == null) {
                        Text(text = "Face Detector is null")
                    } else {
                        MainContent(faceDetector = faceDetector)
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        private val REQUIRED_PERMISSIONS =
            mutableListOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
            ).toTypedArray()
    }
}


@Composable
fun DisplayDrawableImageWithGrayscaleToggle() {
    var isGrayscale by remember { mutableStateOf(false) } // State to toggle grayscale

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        // Display the image
        Image(
            painter = painterResource(id = R.drawable.test), // Replace with your drawable resource
            contentDescription = "Sample Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Crop,
            colorFilter = if (isGrayscale) {
                ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) }) // Grayscale
            } else {
                null // Original
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Button to toggle grayscale
        Button(
            onClick = { isGrayscale = !isGrayscale },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = if (isGrayscale) "Show Original" else "Convert to Grayscale")
        }
    }
}

@Preview
@Composable
fun PreviewDisplayDrawableImageWithGrayscaleToggle() {
    DisplayDrawableImageWithGrayscaleToggle()
}

@Composable
fun BrightnessImageToggle(originalBitmap: Bitmap) {
    var brightness by remember { mutableStateOf(1f) } // Brightness adjustment factor (1f = normal)

    // Create a ColorMatrix for brightness adjustment
    val colorMatrix = ColorMatrix().apply {
        setToScale(brightness, brightness, brightness, 1f) // Adjust RGB channels
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Display the image with brightness adjustment
        Image(
            bitmap = originalBitmap.asImageBitmap(),
            contentDescription = "Adjustable Brightness Image",
            modifier = Modifier.size(300.dp),
            colorFilter = ColorFilter.colorMatrix(colorMatrix) // Apply the brightness filter
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Slider to adjust brightness
        Slider(
            value = brightness,
            onValueChange = { brightness = it },
            valueRange = 0.5f..2f, // Brightness range (0.5 = darker, 2 = brighter)
            modifier = Modifier.padding(horizontal = 16.dp)
        )


    }
}

@Preview
@Composable
fun Prev()
{
    val context = LocalContext.current
    val originalBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.test)
    BrightnessImageToggle(originalBitmap= originalBitmap)
}




// Lớp lưu thông tin ảnh
data class ImageInfo(
    val name: String,
    val width: Int,
    val height: Int,
    val bitmap: Bitmap
)

// Hàm tải ảnh
fun loadImageInfo(context: Context, resourceId: Int): ImageInfo? {
    return try {
        val resources = context.resources
        val bitmap = BitmapFactory.decodeResource(resources, resourceId)
        val name = resources.getResourceEntryName(resourceId)
        ImageInfo(
            name = name,
            width = bitmap.width,
            height = bitmap.height,
            bitmap = bitmap
        )
    } catch (e: Exception) {
        Log.e("MainActivity", "Lỗi khi tải ảnh: ${e.message}")
        null
    }
}

// Composable hiển thị thông tin ảnh
@Composable
fun ImageDetailsScreen(imageName: String, resolution: String, bitmap: Bitmap) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Tên ảnh: $imageName", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Độ phân giải: $resolution", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Ảnh $imageName",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
    }
}

// Composable hiển thị lỗi
@Composable
fun ErrorScreen(errorMessage: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = errorMessage, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewImageDetailsScreen() {
    ImageDetailsScreen(
        imageName = "sample_image",
        resolution = "1920 x 1080",
        bitmap = Bitmap.createBitmap(1920, 1080, Bitmap.Config.ARGB_8888)
    )
}