package com.nicholssoftware.jonslearningappandroid.domain.auth

import javax.inject.Inject

class SendEmailVerificationUseCase @Inject constructor(
    private val authenticator: Authenticator
) {
    operator fun invoke(completion: (Boolean) -> Unit) {
        authenticator.sendEmailVerification(completion)
    }
}