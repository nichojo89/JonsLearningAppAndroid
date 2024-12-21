package com.nicholssoftware.jonslearningappandroid.domain.auth

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser

interface Authenticator {
    fun signUp(email: String, password: String, completion: (Boolean, String?) -> Unit)
    fun signIn(email: String, password: String, completion: (Boolean, String?) -> Unit)
    fun signInWithGoogle(account: GoogleSignInAccount, completion: (Boolean) -> Unit)
    fun sendEmailVerification(completion: (Boolean) -> Unit)
}