package com.darkwhite.opencvfacedetection

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.Card
import androidx.compose.ui.graphics.asImageBitmap
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
