package com.nicholssoftware.jonslearningappandroid.domain.auth.usecase

import com.nicholssoftware.jonslearningappandroid.domain.auth.repository.Authenticator
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authenticator: Authenticator
) {
    operator fun invoke(email: String, password: String, completion: (Boolean, String?) -> Unit) {
        authenticator.signUp(email, password, completion)
    }
}