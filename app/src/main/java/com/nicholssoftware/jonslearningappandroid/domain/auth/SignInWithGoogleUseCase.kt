package com.nicholssoftware.jonslearningappandroid.domain.auth

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val authenticator: Authenticator
) {
    operator fun invoke(account: GoogleSignInAccount, completion: (Boolean) -> Unit){
        authenticator.signInWithGoogle(account, completion)
    }
}