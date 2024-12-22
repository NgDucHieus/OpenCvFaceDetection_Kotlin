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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.Dp
import androidx.navigation.compose.rememberNavController
import com.image.cropview.CropType
import com.image.cropview.EdgeType
import com.image.cropview.ImageCrop

@OptIn(ExperimentalMaterial3Api::class)
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
        Row(
            modifier = Modifier
                .fillMaxWidth() // Ensure the row spans the width of the screen
                , // Align the row to the bottom
            horizontalArrangement = Arrangement.SpaceBetween // Space buttons to the left and right
        )

        {
        IconButton(
            onClick = {}
        )
        {
            Icon(
                painter = painterResource(id = R.drawable.close), // Replace with your rotate icon
                contentDescription = "Rotate Icon",
                modifier = Modifier.size(32.dp)
                ,
                tint = Color.Unspecified // Use the intrinsic color of the drawable
            )
        }
        IconButton(
            onClick = {}
        )
         {
                Icon(
                    painter = painterResource(id = R.drawable.check), // Replace with your rotate icon
                    contentDescription = "Rotate Icon",
                    modifier = Modifier.size(64.dp)
                    ,
                    tint = Color.Unspecified // Use the intrinsic color of the drawable
                )
        }
            }
    }
}

//@Preview
//@Composable
//fun MainContent() {
//    val context = LocalContext.current
//    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.test) // Replace with your drawable
//
//    ImageCropper(bitmap)
//}

@Preview(showBackground = true)
@Composable
fun MainContent() {
    val context = LocalContext.current
    val originalBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.test) // Replace with your drawable

    // State to hold the transformed bitmap
    var transformedBitmap by remember { mutableStateOf(originalBitmap) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Rotate and Flip functionality
        RotateAndFlipImage(
            originalBitmap = originalBitmap,
            onTransformedBitmap = { transformedBitmap = it } // Update the transformed bitmap
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Crop the transformed bitmap
        ImageCropper(bitmap = transformedBitmap)
    }
}

@Composable
fun RotateAndFlipImage(
    originalBitmap: Bitmap,
    onTransformedBitmap: (Bitmap) -> Unit // Callback to return the transformed bitmap
) {
    var rotationAngle by remember { mutableStateOf(0f) } // Rotation angle
    var flippedHorizontally by remember { mutableStateOf(false) } // Horizontal flip state
    var flippedVertically by remember { mutableStateOf(false) } // Vertical flip state

    // Create a transformed bitmap
    val transformedBitmap = remember(rotationAngle, flippedHorizontally, flippedVertically) {
        var bitmap = originalBitmap
        if (flippedHorizontally) bitmap = flipBitmap(bitmap, horizontal = true)
        if (flippedVertically) bitmap = flipBitmap(bitmap, horizontal = false)
        rotateBitmap(bitmap, rotationAngle)
    }

    // Trigger the callback whenever the bitmap changes
    LaunchedEffect(transformedBitmap) {
        onTransformedBitmap(transformedBitmap)
    }

    Column(
        modifier = Modifier

            .background(color = Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {


        Spacer(modifier = Modifier.height(16.dp))

        // Row to position Slider and Buttons horizontally
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // Space buttons and slider evenly
        ) {
            // Flip Horizontally Button (IconButton)
            IconButton(
                onClick = { flippedHorizontally = !flippedHorizontally } // Toggle horizontal flip
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.f66), // Replace with your horizontal flip icon
                    contentDescription = "Flip Horizontally Icon",
                    modifier = Modifier.size(32.dp),
                    tint = Color.Unspecified // Use the intrinsic color of the drawable
                )
            }

            // Slider to adjust rotation angle
            Slider(
                value = rotationAngle,
                onValueChange = { rotationAngle = it },
                valueRange = 0f..360f, // Allow rotation from 0 to 360 degrees
                modifier = Modifier.weight(1f) // Let the slider take the available space
            )

            // Rotate Button (IconButton)
            IconButton(
                onClick = { rotationAngle = (rotationAngle + 90f) % 360f }, // Increment rotation by 90 degrees
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.rotate_100), // Replace with your rotate icon
                    contentDescription = "Rotate Icon",
                    modifier = Modifier.size(32.dp),
                    tint = Color.Unspecified // Use the intrinsic color of the drawable
                )
            }
        }
    }
}

