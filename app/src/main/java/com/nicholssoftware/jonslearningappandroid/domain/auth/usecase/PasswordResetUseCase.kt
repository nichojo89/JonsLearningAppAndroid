package com.nicholssoftware.jonslearningappandroid.domain.auth.usecase

import com.nicholssoftware.jonslearningappandroid.domain.auth.repository.Authenticator
import javax.inject.Inject

class PasswordResetUseCase @Inject constructor(
    private val authenticator: Authenticator
) {
    operator fun invoke(email: String, onResult: (Boolean) -> Unit){
        authenticator.sendPasswordResetEmail(email, onResult)
    }
}