package com.nicholssoftware.jonslearningappandroid.ui.characters.character_gen

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nicholssoftware.jonslearningappandroid.domain.generate_character.usecase.GenerateCharacterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterGeneratorViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val generateCharacterUseCase: GenerateCharacterUseCase
) : ViewModel(){
    private var _isImageSet = mutableStateOf(false)
    var isImageSet: State<Boolean> = _isImageSet

    private val _isSelectImage = mutableStateOf(false)
    val isSelectImage : State<Boolean> = _isSelectImage

    private val _title = mutableStateOf("Describe character")
    val title: State<String> = _title

    private var _selectedImageUri = mutableStateOf<Uri?>(null)
    var selectedImageUri : State<Uri?> = _selectedImageUri

    private val _prompt = mutableStateOf("Cyberpunk warrior, futuristic, sci fi, bearded man")
    val prompt: State<String> = _prompt

    private val _generateCharacterEnabled = mutableStateOf(false)
    val generateCharacterEnabled: State<Boolean> = _generateCharacterEnabled

    private val _generatedImage = mutableStateOf("")
    val generatedImage : State<String> = _generatedImage

    private val _isGeneratedImage = mutableStateOf(false)
    val isGeneratedImage : State<Boolean> = _isGeneratedImage

    fun updatePrompt(prompt: String){
        _prompt.value = prompt
        updateGenerateCharacterEnabled()
    }

    fun updateSelectImage(isSelectImage: Boolean){
        _isSelectImage.value = isSelectImage
        updateGenerateCharacterEnabled()
    }
    fun updateGenerateCharacterEnabled(){
        _generateCharacterEnabled.value = _prompt.value.isNotEmpty() || _selectedImageUri.value != null
    }

    fun updateSelectedImageUri(uri: Uri?){
        _selectedImageUri.value = uri
    }

    fun updateIsImageSet(isImageSet: Boolean){
        _isImageSet.value = isImageSet
    }

    fun updateIsGeneratedImage(isGeneratedImage: Boolean){
        _isGeneratedImage.value = isGeneratedImage
    }

    fun handleTakePictureResult(isSuccess: Boolean) {
        if (isSuccess) {
            _selectedImageUri.value?.let {
                try {
                    context.contentResolver.openInputStream(it)?.use {
                        updateIsImageSet(true)
                        _isGeneratedImage.value = false
                    } ?: run {
                        updateSelectedImageUri(null)
                        showToast("Image file not found after capture")
                    }
                } catch (e: Exception) {
                    updateSelectedImageUri(null)
                    showToast("Error reading image: ${e.message}")
                }
            } ?: updateSelectedImageUri(null)
        } else {
            showToast("Failed to capture image")
            updateSelectedImageUri(null)
        }
        _isSelectImage.value = false
    }

    fun createImageFile(): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "IMG_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/MyApp")
        }

        return try {
            val resolver = context.contentResolver
            resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues).also {
                Log.d("FileCreation", "Image file created: $it")
            }
        } catch (e: Exception) {
            Log.e("FileCreation", "Failed to create image file: ${e.message}")
            null
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun generateCharacter(){
        if(_generateCharacterEnabled.value){
            viewModelScope.launch {
                val response = generateCharacterUseCase.invoke(_selectedImageUri.value, _prompt.value)
                if(response.isSuccess){
                   response.getOrNull()?.images?.first()?.let {
                       _generatedImage.value = it
                       _isGeneratedImage.value = true
                   }
                }
            }
        }
    }
}