package com.nicholssoftware.jonslearningappandroid.domain.network.model

sealed class ResponseResult<out T> {
    object Loading : ResponseResult<Nothing>()
    data class Success<out T>(val data: T) : ResponseResult<T>()
    data class Error(val exception: Throwable) : ResponseResult<Nothing>()
}