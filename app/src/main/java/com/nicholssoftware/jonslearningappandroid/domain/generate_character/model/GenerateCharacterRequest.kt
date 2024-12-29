package com.nicholssoftware.jonslearningappandroid.domain.generate_character.model

import okhttp3.MultipartBody
import okhttp3.RequestBody

data class GenerateCharacterRequest(
    val image: MultipartBody.Part,
    val prompt: RequestBody
)