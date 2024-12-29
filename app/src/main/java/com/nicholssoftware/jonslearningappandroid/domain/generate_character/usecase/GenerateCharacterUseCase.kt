package com.nicholssoftware.jonslearningappandroid.domain.generate_character.usecase

import android.net.Uri
import androidx.core.net.toFile
import com.nicholssoftware.jonslearningappandroid.data.image.repository.ImageRepository
import com.nicholssoftware.jonslearningappandroid.domain.generate_character.model.GenerateCharacterResponse
import java.io.File
import javax.inject.Inject

class GenerateCharacterUseCase @Inject constructor(
    private val imageRepository: ImageRepository) {
    suspend operator fun invoke(imageUri: Uri?, prompt: String): Result<GenerateCharacterResponse> {
        val file: File? = imageUri?.toFile()
        return imageRepository.sendImageToApi(file,prompt)
    }
}