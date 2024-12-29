package com.nicholssoftware.jonslearningappandroid.data.network

import com.nicholssoftware.jonslearningappandroid.domain.network.model.ResponseResult
import kotlinx.coroutines.delay
import retrofit2.Response
import javax.inject.Inject

class ApiClient @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun <T> makeRequest(
        apiCall: suspend () -> Response<T>,
        maxRetry: Int = 3
    ): ResponseResult<T> {
        var attempt = 0

        while (attempt < maxRetry) {
            attempt++
            try {
                val response = apiCall.invoke()
                if (response.isSuccessful) {
                    response.body()?.let {
                        return ResponseResult.Success(it)
                    } ?: return ResponseResult.Error(Exception("Empty Response Body"))
                } else {
                    return ResponseResult.Error(Exception("Error ${response.code()}"))
                }
            } catch (e: Exception) {
                if (attempt == maxRetry) {
                    return ResponseResult.Error(e)
                }
            }

            // 1 second delay between retries
            delay(1000)
        }

        return ResponseResult.Error(Exception("Max retries reached"))
    }
}