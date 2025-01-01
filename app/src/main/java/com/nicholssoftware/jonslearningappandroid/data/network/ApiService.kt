package com.nicholssoftware.jonslearningappandroid.data.network

import com.nicholssoftware.jonslearningappandroid.domain.generate_character.model.GenerateCharacterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("/ImageToImage/GenerateCharacter")
    suspend fun GenerateCharacter(
        @Part image: MultipartBody.Part,
        @Part("prompt") prompt: RequestBody
    ): Response<GenerateCharacterResponse>
}