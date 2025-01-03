package com.nicholssoftware.jonslearningappandroid.domain.auth.usecase

import com.nicholssoftware.jonslearningappandroid.domain.auth.repository.Authenticator
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authenticator: Authenticator
) {
    operator fun invoke(email: String, password: String, rememberUser: Boolean, completion: (Boolean, String?) -> Unit){
        authenticator.signIn(email, password, rememberUser, completion)
    }
}