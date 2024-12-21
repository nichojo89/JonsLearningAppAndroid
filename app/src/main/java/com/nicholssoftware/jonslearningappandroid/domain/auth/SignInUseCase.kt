package com.nicholssoftware.jonslearningappandroid.domain.auth

import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authenticator: Authenticator
) {
    operator fun invoke(email: String, password: String, completion: (Boolean, String?) -> Unit){
        authenticator.signIn(email, password, completion)
    }
}