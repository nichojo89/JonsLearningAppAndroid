package com.nicholssoftware.jonslearningappandroid.data.image.repository

import android.content.Context
import android.net.Uri
import android.util.Base64
import com.nicholssoftware.jonslearningappandroid.data.network.ApiService
import com.nicholssoftware.jonslearningappandroid.domain.generate_character.model.GenerateCharacterRequest
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
            val base64Image = image?.let {
                val bytes = it.readBytes()
                // Add the base64 prefix that many APIs expect
                "data:image/jpeg;base64," + Base64.encodeToString(bytes, Base64.NO_WRAP)
            }

            val request = GenerateCharacterRequest(
                image = base64Image?.take(400),
                prompt = prompt
            )

            // Debug logging
//            Log.d("API_DEBUG", "Sending request: $request")

            val response = apiService.GenerateCharacter(request)
//            Log.d("API_DEBUG", "Response code: ${response.code()}")

            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                // Log the error body
                val errorBody = response.errorBody()?.string()
//                Log.e("API_DEBUG", "Error body: $errorBody")
                Result.failure(Exception("Error: ${response.code()} - $errorBody"))
            }
        } catch (e: Exception) {
//            Log.e("API_DEBUG", "Exception", e)
            Result.failure(e)
        }
    }
}
