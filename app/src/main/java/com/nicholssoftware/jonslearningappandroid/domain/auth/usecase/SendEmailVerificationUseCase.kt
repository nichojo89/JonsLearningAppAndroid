package com.nicholssoftware.jonslearningappandroid.domain.auth.usecase

import com.nicholssoftware.jonslearningappandroid.domain.auth.repository.Authenticator
import javax.inject.Inject

class SendEmailVerificationUseCase @Inject constructor(
    private val authenticator: Authenticator
) {
    operator fun invoke(completion: (Boolean) -> Unit) {
        authenticator.sendEmailVerification(completion)
    }
}