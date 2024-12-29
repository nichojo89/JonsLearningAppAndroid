package com.nicholssoftware.jonslearningappandroid.data.network.intercepters

import com.nicholssoftware.jonslearningappandroid.domain.auth.repository.Authenticator
import com.nicholssoftware.jonslearningappandroid.domain.auth.repository.FirebaseAuthenticator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AuthInterceptor(private val firebaseAuthenticator: FirebaseAuthenticator) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var token: String? = null
        val job = GlobalScope.launch(Dispatchers.IO) {
            token = firebaseAuthenticator.getToken()
        }

        runBlocking {
            job.join()
        }

        if (token != null) {
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()

            return chain.proceed(request)
        } else {
            throw IOException("Failed to retrieve token")
        }
    }
}