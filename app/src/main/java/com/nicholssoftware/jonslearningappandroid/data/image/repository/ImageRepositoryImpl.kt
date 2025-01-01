package com.nicholssoftware.jonslearningappandroid.data.image.repository

import android.content.Context
import android.net.Uri
import com.nicholssoftware.jonslearningappandroid.data.network.ApiService
import com.nicholssoftware.jonslearningappandroid.domain.generate_character.model.GenerateCharacterResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiService: ApiService
) : ImageRepository {
    override fun uriToFile(uri: Uri): File? {
        val contentResolver = context.contentResolver
        val fileName = "temp_image_${System.currentTimeMillis()}"
        val tempFile = File(context.cacheDir, fileName)
        tempFile.createNewFile()

        contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(tempFile).use { outputStream ->
                copyStream(inputStream, outputStream)
            }
        }
        return tempFile
    }

    private fun copyStream(input: InputStream, output: FileOutputStream) {
        val buffer = ByteArray(1024)
        var bytesRead: Int
        while (input.read(buffer).also { bytesRead = it } != -1) {
            output.write(buffer, 0, bytesRead)
        }
    }

    override suspend fun sendImageToApi(image: File?, prompt: String): Result<GenerateCharacterResponse> {
        return try {
            val multiPartImage = MultipartBody.Part.createFormData("image", image?.name, image?.asRequestBody()!!)
            val requestBodyPrompt = prompt.toRequestBody("text/plain".toMediaTypeOrNull())
            val response = apiService.GenerateCharacter(
                multiPartImage,
                requestBodyPrompt
            )
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                Result.failure(Exception("Error: ${response.code()} - $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
