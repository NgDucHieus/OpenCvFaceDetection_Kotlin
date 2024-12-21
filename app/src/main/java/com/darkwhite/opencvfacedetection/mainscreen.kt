package com.darkwhite.opencvfacedetection

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

@Composable
fun GalleryNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "gallery"
    ) {
        composable("gallery") {
            GalleryScreen(navController = navController)
        }
        composable("detail/{imageId}") { backStackEntry ->
            val imageId = backStackEntry.arguments?.getString("imageId")?.toIntOrNull()
            if (imageId != null) {
                ImageDetailScreen(imageResId = imageId)
            }
        }
    }
}

@Composable
fun GalleryScreen(navController: NavHostController) {
    val context = LocalContext.current
    val images = remember {
        listOf(
            R.drawable.test, // Replace with your images
            R.drawable.test, // Replace with your images
            R.drawable.test, // Replace with your images
            R.drawable.test, // Replace with your images
            R.drawable.test, // Replace with your images
            R.drawable.test, // Replace with your images
            R.drawable.test, // Replace with your images
            R.drawable.test, // Replace with your images
            R.drawable.test, // Replace with your images
            R.drawable.test, // Replace with your images
            R.drawable.test, // Replace with your images


        )
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
//        modifier = Modifier.padding(1.dp),
        horizontalArrangement = Arrangement.spacedBy(1.dp) // Space between columns

    ) {
        items(images.size) { index ->
            val imageResId = images[index]
            val bitmap = BitmapFactory.decodeResource(context.resources, imageResId)
            Card(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()
//                    .aspectRatio(1f) // Square cards
                    .clickable {
                        navController.navigate("detail/$imageResId")
                    },
                shape = RoundedCornerShape(20.dp),

            ) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Image $index",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun ImageDetailScreen(imageResId: Int) {
    val context = LocalContext.current
    val bitmap = BitmapFactory.decodeResource(context.resources, imageResId)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Detail View",
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewImageDetailScreen() {
    ImageDetailScreen(imageResId = R.drawable.test)
}

@Preview(showBackground = true)
@Composable
fun PreviewGalleryNavHost() {
    val navController = rememberNavController()
    GalleryNavHost(navController = navController)
}



fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
    val matrix = Matrix().apply {
        postRotate(degrees) // Apply rotation
    }
    return Bitmap.createBitmap(
        bitmap,
        0,
        0,
        bitmap.width,
        bitmap.height,
        matrix,
        true
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RotateImage(originalBitmap: Bitmap) {
    var rotationAngle by remember { mutableStateOf(0f) } // Rotation angle

    // Create a rotated bitmap
    val rotatedBitmap = remember(rotationAngle) {
        rotateBitmap(originalBitmap, rotationAngle)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Display the rotated image
        Image(
            bitmap = rotatedBitmap.asImageBitmap(),
            contentDescription = "Rotated Image",
            modifier = Modifier.size(300.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row {



        // Slider to adjust rotation angle
            Slider(
            value = rotationAngle,
            onValueChange = { rotationAngle = it },
            valueRange = 0f..360f, // Allow rotation from 0 to 360 degrees
            modifier = Modifier.padding(horizontal = 16.dp)
                                .weight(1f)
            )
//            Spacer(modifier = Modifier.width(5.dp))

            IconButton(
                onClick = { rotationAngle = (rotationAngle +90f) % 360f } // Increment rotation by 90 degrees

            ) {
                Icon(
                    contentDescription = "",
                    painter = painterResource(R.drawable.r)
                )
            }
        }

    }
}
@Preview (showBackground = true)
@Composable
fun Prei()
{
    val context = LocalContext.current
    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.test) // Replace with your drawable
    RotateImage(originalBitmap = bitmap)
}


fun flipBitmap(bitmap: Bitmap, horizontal: Boolean): Bitmap {
    val matrix = Matrix().apply {
        if (horizontal) {
            preScale(-1f, 1f) // Flip horizontally
        } else {
            preScale(1f, -1f) // Flip vertically
        }
    }
    return Bitmap.createBitmap(
        bitmap,
        0,
        0,
        bitmap.width,
        bitmap.height,
        matrix,
        true
    )
}
@Composable
fun RotateAndFlipImage(originalBitmap: Bitmap) {
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Display the transformed image
        Image(
            bitmap = transformedBitmap.asImageBitmap(),
            contentDescription = "Transformed Image",
            modifier = Modifier.size(300.dp)
        )

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


@Preview (showBackground = true)
@Composable
fun final()
{
    val context = LocalContext.current
    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.test)
    RotateAndFlipImage(bitmap)

}
