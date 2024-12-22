package com.darkwhite.opencvfacedetection


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog


fun negativeImage(bitmap: Bitmap): Bitmap {
    // Create a mutable copy of the bitmap
    val width = bitmap.width
    val height = bitmap.height
    val resultBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)

    // Iterate through all pixels
    for (x in 0 until width) {
        for (y in 0 until height) {
            val pixel = bitmap.getPixel(x, y)

            // Extract the RGB components
            val r = (pixel shr 16 and 0xFF)
            val g = (pixel shr 8 and 0xFF)
            val b = (pixel and 0xFF)

            // Compute the negative values for each channel
            val newR = (255 - r).coerceIn(0, 255)
            val newG = (255 - g).coerceIn(0, 255)
            val newB = (255 - b).coerceIn(0, 255)

            // Recreate the pixel with the negative RGB values
            val newPixel = (0xFF shl 24) or (newR shl 16) or (newG shl 8) or newB

            // Set the pixel in the result bitmap
            resultBitmap.setPixel(x, y, newPixel)
        }
    }

    return resultBitmap
}

fun applyThreshold(bitmap: Bitmap, threshold: Int): Bitmap {
    val width = bitmap.width
    val height = bitmap.height
    val resultBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)

    for (x in 0 until width) {
        for (y in 0 until height) {
            val pixel = bitmap.getPixel(x, y)

            // Convert to grayscale
            val r = (pixel shr 16 and 0xFF)
            val g = (pixel shr 8 and 0xFF)
            val b = (pixel and 0xFF)
            val gray = (0.3 * r + 0.59 * g + 0.11 * b).toInt()

            // Apply the threshold
            val newPixel = if (gray > threshold) {
                (0xFF shl 24) or (255 shl 16) or (255 shl 8) or 255 // White pixel
            } else {
                (0xFF shl 24) or (0 shl 16) or (0 shl 8) or 0 // Black pixel
            }

            resultBitmap.setPixel(x, y, newPixel)
        }
    }

    return resultBitmap
}



fun bitPlaneSlicing(bitmap: Bitmap, bitPlane: Int): Bitmap {
    val width = bitmap.width
    val height = bitmap.height
    val resultBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)

    for (x in 0 until width) {
        for (y in 0 until height) {
            val pixel = bitmap.getPixel(x, y)

            // Convert to grayscale
            val r = (pixel shr 16 and 0xFF)
            val g = (pixel shr 8 and 0xFF)
            val b = (pixel and 0xFF)
            val gray = (0.3 * r + 0.59 * g + 0.11 * b).toInt()

            // Extract the bit value
            val bitValue = (gray shr bitPlane) and 1
            val newPixel = if (bitValue == 1) {
                (0xFF shl 24) or (255 shl 16) or (255 shl 8) or 255 // White pixel
            } else {
                (0xFF shl 24) or (0 shl 16) or (0 shl 8) or 0 // Black pixel
            }

            resultBitmap.setPixel(x, y, newPixel)
        }
    }

    return resultBitmap
}

fun greyLevelSlicing(bitmap: Bitmap, lowerBound: Int, upperBound: Int, preserve: Boolean): Bitmap {
    val width = bitmap.width
    val height = bitmap.height
    val resultBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)

    for (x in 0 until width) {
        for (y in 0 until height) {
            val pixel = bitmap.getPixel(x, y)

            // Convert to grayscale
            val r = (pixel shr 16 and 0xFF)
            val g = (pixel shr 8 and 0xFF)
            val b = (pixel and 0xFF)
            val gray = (0.3 * r + 0.59 * g + 0.11 * b).toInt()

            // Perform slicing
            val newPixel = if (gray in lowerBound..upperBound) {
                if (preserve) { // Preserve intensities in the range
                    pixel
                } else { // Highlight intensities in the range
                    (0xFF shl 24) or (255 shl 16) or (255 shl 8) or 255 // White pixel
                }
            } else {
                (0xFF shl 24) or (0 shl 16) or (0 shl 8) or 0 // Black pixel
            }

            resultBitmap.setPixel(x, y, newPixel)
        }
    }

    return resultBitmap
}

@Composable
fun ThresholdingScreen(bitmap: Bitmap) {
    val thresholdedBitmap = remember { applyThreshold(bitmap, threshold = 128) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Original Image")
        Image(bitmap = bitmap.asImageBitmap(), contentDescription = "Original Image", modifier = Modifier.size(200.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Text("Thresholded Image")
        Image(bitmap = thresholdedBitmap.asImageBitmap(), contentDescription = "Thresholded Image", modifier = Modifier.size(200.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewThresholdingScreen() {
    val context = LocalContext.current
    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.test) // Replace with your drawable
    ThresholdingScreen(bitmap = bitmap)
}

@Composable
fun GreyLevelSlicingScreen(bitmap: Bitmap) {
    val slicedBitmap = remember { greyLevelSlicing(bitmap, 50, 200, preserve = false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Original Image")
        Image(bitmap = bitmap.asImageBitmap(), contentDescription = "Original Image", modifier = Modifier.size(200.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Text("Grey Level Slicing")
        Image(bitmap = slicedBitmap.asImageBitmap(), contentDescription = "Grey Level Slicing", modifier = Modifier.size(200.dp))
    }
}


@Composable
fun BitPlaneSlicingScreen(bitmap: Bitmap) {
    val bitPlaneBitmap = remember { bitPlaneSlicing(bitmap, bitPlane = 3) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Original Image")
        Image(bitmap = bitmap.asImageBitmap(), contentDescription = "Original Image", modifier = Modifier.size(200.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Text("Bit Plane Slicing (Plane 3)")
        Image(bitmap = bitPlaneBitmap.asImageBitmap(), contentDescription = "Bit Plane Slicing", modifier = Modifier.size(200.dp))
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewGreyLevelSlicingScreen() {
    val context = LocalContext.current
    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.test) // Replace with your drawable
    GreyLevelSlicingScreen(bitmap = bitmap)
}
@Preview(showBackground = true)
@Composable
fun PreviewBitPlaneSlicingScreen() {
    val context = LocalContext.current
    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.test) // Replace with your drawable
    BitPlaneSlicingScreen(bitmap = bitmap)
}

@Composable
fun NegativeImageScreen(bitmap: Bitmap) {
    val negativeBitmap = remember { negativeImage(bitmap) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Original Image")
        Image(bitmap = bitmap.asImageBitmap(), contentDescription = "Original Image", modifier = Modifier.size(200.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Text("Negative Image")
        Image(bitmap = negativeBitmap.asImageBitmap(), contentDescription = "Negative Image", modifier = Modifier.size(200.dp))
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewNegativeImageScreen() {
    val context = LocalContext.current
    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.test) // Replace with your drawable
    NegativeImageScreen(bitmap = bitmap)
}


fun logarithmicTransformation(bitmap: Bitmap): Bitmap {
    // Create a mutable copy of the bitmap
    val width = bitmap.width
    val height = bitmap.height
    val resultBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)

    // Define the scaling factor for the logarithmic transformation
    val scaleFactor = 255 / Math.log(256.0)

    // Iterate through all pixels
    for (x in 0 until width) {
        for (y in 0 until height) {
            val pixel = bitmap.getPixel(x, y)

            // Extract the RGB components
            val r = (pixel shr 16 and 0xFF)
            val g = (pixel shr 8 and 0xFF)
            val b = (pixel and 0xFF)

            // Apply the logarithmic transformation
            val newR = (scaleFactor * Math.log(1.0 + r)).toInt().coerceIn(0, 255)
            val newG = (scaleFactor * Math.log(1.0 + g)).toInt().coerceIn(0, 255)
            val newB = (scaleFactor * Math.log(1.0 + b)).toInt().coerceIn(0, 255)

            // Recreate the pixel
            val newPixel = (0xFF shl 24) or (newR shl 16) or (newG shl 8) or newB

            // Set the pixel in the result bitmap
            resultBitmap.setPixel(x, y, newPixel)
        }
    }

    return resultBitmap
}

@Composable
fun LogarithmicTransformationScreen(bitmap: Bitmap) {
    val logTransformedBitmap = remember { logarithmicTransformation(bitmap) }

    Column {
        Text("Original Image")
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Original Image",
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Logarithmic Transformed Image")
        Image(
            bitmap = logTransformedBitmap.asImageBitmap(),
            contentDescription = "Logarithmic Image",
            modifier = Modifier.size(200.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLogarithmicTransformationScreen() {
    val context = LocalContext.current
    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.test) // Replace with your drawable
    LogarithmicTransformationScreen(bitmap = bitmap)

}


@Composable
fun ImageProcessingShowcase(originalBitmap: Bitmap) {
    // State for showing the full-screen image
    var selectedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Processed images
    val processedImages = listOf(
        Pair("Negative", negativeImage(originalBitmap)),
        Pair("Thresholding", applyThreshold(originalBitmap, 128)),
        Pair("Grey Level Slicing", greyLevelSlicing(originalBitmap, 50, 200, preserve = false)),
        Pair("Bit Plane Slicing", bitPlaneSlicing(originalBitmap, 3)),
        Pair("Logarithmic", logarithmicTransformation(originalBitmap))
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Display the original image
        Text("Original Image", modifier = Modifier.padding(8.dp))
        Image(
            bitmap = originalBitmap.asImageBitmap(),
            contentDescription = "Original Image",
            modifier = Modifier
                .size(200.dp)
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Processed images in a LazyRow
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(processedImages) { (title, bitmap) ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(title, modifier = Modifier.padding(4.dp))

                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = title,
                        modifier = Modifier
                            .size(150.dp)
                            .clickable {
                                selectedBitmap = bitmap // Open the selected image in full-screen
                            }
                            .padding(4.dp)
                    )
                }
            }
        }

        // Full-screen dialog for the selected image
        if (selectedBitmap != null) {
            Dialog(onDismissRequest = { selectedBitmap = null }) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                        .clickable { selectedBitmap = null },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        bitmap = selectedBitmap!!.asImageBitmap(),
                        contentDescription = "Selected Image",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}






@Preview(showBackground = true)
@Composable
fun PreviewImageProcessingShowcase() {
    val context = LocalContext.current
    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.test) // Replace with your drawable
    ImageProcessingShowcase(originalBitmap = bitmap)
}
