package com.nicholssoftware.jonslearningappandroid.domain.auth

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

interface Authenticator {
    fun signUp(email: String, password: String, completion: (Boolean, String?) -> Unit)
    fun signIn(email: String, password: String, rememberUser: Boolean, completion: (Boolean, String?) -> Unit)
    fun signInWithGoogle(account: GoogleSignInAccount, rememberUser: Boolean, completion: (Boolean) -> Unit)
    fun sendEmailVerification(completion: (Boolean) -> Unit)
    fun sendPasswordResetEmail(email: String, onResult: (Boolean) -> Unit)
}