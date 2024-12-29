package com.nicholssoftware.jonslearningappandroid.data.network

import com.nicholssoftware.jonslearningappandroid.domain.generate_character.model.GenerateCharacterRequest
import com.nicholssoftware.jonslearningappandroid.domain.generate_character.model.GenerateCharacterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET

interface ApiService {
    @GET("ImageToImage/GenerateCharacter")
    suspend fun fetchData(@Body requestBody: GenerateCharacterRequest): Response<GenerateCharacterResponse>
}