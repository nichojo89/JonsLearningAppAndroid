package com.nicholssoftware.jonslearningappandroid.data.image.repository

import android.net.Uri
import com.nicholssoftware.jonslearningappandroid.domain.generate_character.model.GenerateCharacterResponse
import java.io.File

interface ImageRepository {
    fun uriToFile(uri: Uri): File?
    suspend fun sendImageToApi(image: File?, prompt: String): Result<GenerateCharacterResponse>
}