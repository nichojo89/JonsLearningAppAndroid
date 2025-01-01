package com.nicholssoftware.jonslearningappandroid.domain.generate_character.model

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part

data class GenerateCharacterRequest(
//     image
    @SerializedName("image") @Part val image: MultipartBody.Part?,
    @SerializedName("prompt") val prompt: String?
)