package com.nicholssoftware.jonslearningappandroid.domain.generate_character.model

import okhttp3.MultipartBody

data class GenerateCharacterResponse(
    val image: MultipartBody.Part
)