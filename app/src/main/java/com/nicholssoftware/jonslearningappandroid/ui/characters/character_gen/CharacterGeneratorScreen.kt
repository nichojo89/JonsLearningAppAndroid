package com.nicholssoftware.jonslearningappandroid.ui.characters.character_gen

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.nicholssoftware.jonslearningappandroid.ui.common_components.image.ImageUploader
import com.nicholssoftware.jonslearningappandroid.ui.common_components.text_field.DOSStyleOutlinedTextField

const val CAMERA_PERMISSION_REQUEST_CODE = 1001

@Composable
fun CharacterGeneratorScreen(
    prompt: State<String>,
    title: State<String>,
    generateCharacterEnabled: State<Boolean>,
    updatePrompt: (String) -> Unit,
    generateCharacter: () -> Unit,
    isImageSelected : State<Boolean>,
    selectedImageUri : State<Uri?>,
    isImageSet: State<Boolean>,
    updateSelectImage: (isSelectImage: Boolean) -> Unit,
    updateSelectedImageUri: (uri: Uri?) -> Unit,
    handleTakePictureResult: (isSuccess: Boolean) -> Unit,
    createImageFile: () -> Uri?,
    takePicture: (newImplementation: () -> Unit) -> Unit = {},
    updateIsImageSet: (isImageSet: Boolean) -> Unit = {},
    generatedImage: State<String>,
    isGeneratedImage : State<Boolean>,
    updateIsGeneratedImage: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { isSuccess ->
            handleTakePictureResult.invoke(isSuccess)
        }
    )
    //Invoked from MainActivity.onRequestPermissionsResult
    val takePic: () -> Unit = {
        selectedImageUri.value?.let {
            takePictureLauncher.launch(it)
        }
    }
    takePicture(takePic)
    val selectPictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri: Uri? ->
            updateSelectedImageUri.invoke(uri)
            updateSelectImage.invoke(false)
            updateIsImageSet(true)
            updateIsGeneratedImage(false)
        }
    )
    Scaffold(
        content = { paddingValues ->
            BoxWithConstraints(
                modifier = Modifier.background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF8A2BE2),
                            Color(0xFF9370DB),
                            Color(0xFF8A2BE2)
                        )
                    )
                )
            ) {
                val width = maxWidth
                val height = maxHeight
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp)
                        .padding(top = paddingValues.calculateTopPadding() + 12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ImageUploader(
                            modifier = Modifier.clickable {
                                updateSelectImage(true)
                            },
                            width,
                            selectedImageUri.value,
                            isImageSet.value,
                            generatedImage.value,
                            isGeneratedImage.value,
                            context,
                        )
                        Text("Select an image to base your character from",
                            fontSize = 16.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    DOSStyleOutlinedTextField(
                        Modifier.height(height.div(4)),
                        prompt,
                        title,
                        updatePrompt
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        enabled = generateCharacterEnabled.value,
                        onClick = {
                            generateCharacter.invoke()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 50.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            disabledContentColor = Color.White,
                            containerColor = Color(0xFFAD3689),
                            disabledContainerColor = Color(0x80AD3689)
                        ),
                    ) {
                        Text("Generate")
                    }
                }
                if (isImageSelected.value) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Gray.copy(alpha = 0.5f))
                            .clickable { updateSelectImage.invoke(false) }
                    ) {
                        Row(modifier = Modifier.align(Alignment.Center)) {
                            Spacer(modifier = Modifier.weight(1f))
                            //Take camera picture
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color.White)
                                    .border(
                                        BorderStroke(1.dp, Color.Gray),
                                        RoundedCornerShape(14.dp)
                                    )
                            ) {
                                IconButton(
                                    modifier = Modifier.align(Alignment.Center),
                                    onClick = {
                                        //Create stub file
                                        val photoUri = createImageFile.invoke()
                                        if (photoUri != null) {
                                            updateSelectedImageUri.invoke(photoUri)
                                        }
                                        //Check permissions
                                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                            ActivityCompat.requestPermissions(
                                                context as Activity,
                                                arrayOf(Manifest.permission.CAMERA),
                                                CAMERA_PERMISSION_REQUEST_CODE
                                            )
                                        } else {
                                            //Take picture
                                            takePic.invoke()
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Camera,
                                        contentDescription = "Take Picture",
                                        tint = Color.Black
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color.White)
                                    .border(
                                        BorderStroke(1.dp, Color.Gray),
                                        RoundedCornerShape(14.dp)
                                    )
                            ) {
                                IconButton(
                                    modifier = Modifier.align(Alignment.Center),
                                    onClick = {
                                        selectPictureLauncher.launch(
                                            PickVisualMediaRequest(
                                                ActivityResultContracts.PickVisualMedia.ImageOnly
                                            )
                                        )
                                    }) {
                                    Icon(
                                        tint = Color.Black,
                                        imageVector = Icons.Filled.Image,
                                        contentDescription = "Pick Image"
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    )
}


@Preview
@Composable
fun CharacterGeneratorScreenPreview() {
    val promptState = remember { mutableStateOf("Enter character details here...") }
    val titleState =remember { mutableStateOf("Character Generator")}
    val generateCharacterEnabled = remember {mutableStateOf(false)}
    val isImageSelected = remember {mutableStateOf(false)}
    val selectedImageUri = remember {mutableStateOf<Uri?>(null)}
    val isImageSet = remember {mutableStateOf(false)}
    val generateImage = remember { mutableStateOf("") }
    val isGeneratedImage = remember { mutableStateOf(false) }
    val updatePrompt: (String) -> Unit = { }
    val generateCharacter: () -> Unit = { }
    val updateSelectImage: (Boolean) -> Unit = { }
    val updateSelectedImageUri: (Uri?) -> Unit = { }
    val handleTakePictureResult: (Boolean) -> Unit = { }
    val createImageFile: () -> Uri? = { null }
    val takePicture: (newImplementation: () -> Unit) -> Unit = { }
    val updateIsImageSet: (Boolean) -> Unit = { }
    val updateIsGeneratedImage: (Boolean) -> Unit = {}

    CharacterGeneratorScreen(
        prompt = promptState,
        title = titleState,
        generateCharacterEnabled = generateCharacterEnabled,
        updatePrompt = updatePrompt,
        generateCharacter = generateCharacter,
        isImageSelected = isImageSelected,
        selectedImageUri = selectedImageUri,
        isImageSet = isImageSet,
        updateSelectImage = updateSelectImage,
        updateSelectedImageUri = updateSelectedImageUri,
        handleTakePictureResult = handleTakePictureResult,
        createImageFile = createImageFile,
        takePicture = takePicture,
        updateIsImageSet = updateIsImageSet,
        generatedImage = generateImage,
        isGeneratedImage = isGeneratedImage,
        updateIsGeneratedImage = updateIsGeneratedImage
    )
}