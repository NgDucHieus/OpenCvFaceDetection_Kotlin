package com.darkwhite.opencvfacedetection


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController



fun cropBitmap(bitmap: Bitmap, x: Int, y: Int, width: Int, height: Int): Bitmap {
    val cropWidth = width.coerceAtMost(bitmap.width - x)
    val cropHeight = height.coerceAtMost(bitmap.height - y)
    return Bitmap.createBitmap(bitmap, x, y, cropWidth, cropHeight)
}

@Composable
fun InteractiveCroppingTool(originalBitmap: Bitmap) {
    // Define initial crop rectangle
    var cropStartX by remember { mutableStateOf(100f) } // Top-left X
    var cropStartY by remember { mutableStateOf(100f) } // Top-left Y
    var cropWidth by remember { mutableStateOf(300f) } // Rectangle width
    var cropHeight by remember { mutableStateOf(200f) } // Rectangle height

    val croppedBitmap = remember(cropStartX, cropStartY, cropWidth, cropHeight) {
        cropBitmap(
            originalBitmap,
            cropStartX.toInt(),
            cropStartY.toInt(),
            cropWidth.toInt(),
            cropHeight.toInt()
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Display the original image
        Image(
            bitmap = originalBitmap.asImageBitmap(),
            contentDescription = "Original Image",
            modifier = Modifier.fillMaxSize()
        )

        // Cropping overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            change.consume() // Consume the gesture
                            cropStartX += dragAmount.x
                            cropStartY += dragAmount.y
                        }
                    )
                }
        ) {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                drawRect(
                    color = Color.Black.copy(alpha = 0.5f), // Dim the background
                    size = size
                )
                drawRect(
                    color = Color.White, // Highlight the cropping area
                    topLeft = Offset(cropStartX, cropStartY),
                    size = androidx.compose.ui.geometry.Size(cropWidth, cropHeight),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2f)
                )
            }
        }

        // Display cropped bitmap
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Image(
                bitmap = croppedBitmap.asImageBitmap(),
                contentDescription = "Cropped Image",
                modifier = Modifier.size(150.dp) // Preview the cropped image
            )
        }
    }
}
