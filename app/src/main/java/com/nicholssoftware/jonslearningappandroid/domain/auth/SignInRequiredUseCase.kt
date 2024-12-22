package com.nicholssoftware.jonslearningappandroid.domain.auth

import javax.inject.Inject

class SignInRequiredUseCase @Inject constructor(
    private val authenticator: Authenticator
) {
    operator fun invoke(completion: (Boolean?) -> Unit){
        authenticator.userCanSignIn(completion)
    }
}