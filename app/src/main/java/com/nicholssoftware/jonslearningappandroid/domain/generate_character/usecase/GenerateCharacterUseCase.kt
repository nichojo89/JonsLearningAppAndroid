package com.nicholssoftware.jonslearningappandroid.domain.generate_character.usecase

import android.content.Context
import android.net.Uri
import androidx.core.net.toFile
import com.nicholssoftware.jonslearningappandroid.data.image.repository.ImageRepository
import com.nicholssoftware.jonslearningappandroid.domain.generate_character.model.GenerateCharacterResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class GenerateCharacterUseCase @Inject constructor(
    private val imageRepository: ImageRepository,
    @ApplicationContext private var context: Context
) {
    suspend operator fun invoke(imageUri: Uri?, prompt: String): Result<GenerateCharacterResponse> {
        val file: File? = imageUri?.let { uri ->
            // Create a temporary file
            val tempFile = File.createTempFile("temp_image", ".jpg", context.cacheDir)

            // Copy the content URI to the temporary file
            context.contentResolver.openInputStream(uri)?.use { input ->
                tempFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            tempFile
        }
        return imageRepository.sendImageToApi(file,prompt)
    }
}