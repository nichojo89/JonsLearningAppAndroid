package com.nicholssoftware.jonslearningappandroid.ui.common_components.image

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dagger.hilt.android.qualifiers.ApplicationContext

@Composable
fun ImageUploader(
    modifier: Modifier,
    maxWidth: Dp,
    imageUri: Uri?,
    isImageSet: Boolean,
    generatedImage: String?,
    isGeneratedImage: Boolean,
    @ApplicationContext context: Context
) {
    val width = remember {maxWidth.value.div(1.5)  }
    val height = remember { width.times(1.33) }
    val plusSize = remember { width.div(6) }
    val showImage = imageUri != null && isImageSet

    Box(
        modifier = modifier
            .then(
                if (!showImage) Modifier.fillMaxWidth() else Modifier.wrapContentSize()
            )
            .height(height.dp)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(16.dp),
                clip = false
            )

    ) {
        if (showImage) {
            if(generatedImage?.isNotEmpty() == true && isGeneratedImage){
                val byteArray = Base64.decode(generatedImage.toString(), Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                val imageBitmap = bitmap?.asImageBitmap()

                if (imageBitmap != null) {
                    Image(
                        bitmap = imageBitmap,
                        contentDescription = "Base64 Encoded Image",
                        contentScale = ContentScale.FillHeight,
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(16.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )
                } else {
                    Toast.makeText(context, "Failed to parse gen image", Toast.LENGTH_SHORT).show()
                }
            } else {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUri)
                        .crossfade(true)
                        .listener(
                            onStart = { Log.d("AsyncImage", "Loading started for $imageUri") },
                            onSuccess = { _, _ -> Log.d("AsyncImage", "Image loaded successfully") },
                            onError = { _, throwable -> Log.e("AsyncImage", "Error loading image") }
                        )
                        .build(),
                    contentScale = ContentScale.FillHeight,
                    contentDescription = "Captured Image",
                    modifier = Modifier.fillMaxHeight()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
            }
        } else {
            Canvas(
                modifier = Modifier.matchParentSize()
            ) {
                val gradientBrush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF8A2BE2),
                        Color(0xFF9370DB),
                        Color(0xFFBA55D3)
                    )
                )
                drawRect(
                    color = Color(0xFF4B0082),
                    size = size
                )
                drawRect(
                    brush = gradientBrush,
                    size = size,
                    style = Stroke(width = 12.dp.toPx())
                )
            }
            Text(
                text = "+",
                color = Color.White,
                fontSize = (plusSize).sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Preview
@Composable
fun PreviewImageUploader() {
    val mockUri = Uri.parse("https://www.example.com/sample.jpg") // Replace with a valid image URL or resource URI
    val maxWidth = 300.dp // Set your desired maxWidth here

    // Create a mock image set scenario for preview
    val isImageSet =  false

    val context = LocalContext.current
    ImageUploader(
        modifier = Modifier.padding(16.dp),
        maxWidth = maxWidth,
        imageUri = mockUri,
        isImageSet = isImageSet,
        generatedImage = null,
        isGeneratedImage = false,
        context
    )
}