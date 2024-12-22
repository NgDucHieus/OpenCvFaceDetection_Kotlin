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
import androidx.compose.foundation.layout.offset
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
import com.image.cropview.CropType
import com.image.cropview.EdgeType
import com.image.cropview.ImageCrop

@Composable
fun ImageCropper(bitmap: Bitmap) {
    // Initialize the ImageCrop class
    val imageCrop = remember { ImageCrop(bitmap) }

    // Calculate aspect ratio for proportional scaling
    val aspectRatio = bitmap.width.toFloat() / bitmap.height

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth() // Fill the width of the screen
                .aspectRatio(aspectRatio) // Maintain the image's aspect ratio
                .background(Color.Gray) // Optional: Add a background color
        ) {
            // Display the cropping tool
            imageCrop.ImageCropView(
                modifier = Modifier.fillMaxSize(), // Scale the image to fit within the aspect ratio
                guideLineColor = Color.LightGray,
                guideLineWidth = 2.dp,
                edgeCircleSize = 8.dp,
                showGuideLines = true,
                cropType = CropType.FREE_STYLE,
                edgeType = EdgeType.CIRCULAR
            )
        }

        Button(
            onClick = {
                val croppedBitmap = imageCrop.onCrop()
                // Handle the cropped bitmap (e.g., save or display it)
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Crop")
        }
    }
}

@Preview
@Composable
fun MainContent() {
    val context = LocalContext.current
    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.test) // Replace with your drawable
    ImageCropper(bitmap)
}
