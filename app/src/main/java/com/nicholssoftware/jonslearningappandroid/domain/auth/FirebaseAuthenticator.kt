package com.nicholssoftware.jonslearningappandroid.domain.auth

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import java.lang.Exception
import javax.inject.Inject

class FirebaseAuthenticator @Inject constructor() : Authenticator {
    companion object {
        val firebaseAuth = FirebaseAuth.getInstance()
        var user: FirebaseUser? = firebaseAuth.currentUser
    }

    override fun signUp(email: String, password: String, completion: (Boolean, String?) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    user = firebaseAuth.currentUser
                    completion(task.isSuccessful, null)
                } else {
                    val errorMessage = task.exception?.let { extractSignInErrorMessage(it) }
                    completion(task.isSuccessful, errorMessage)
                }
            }
    }

    override fun signIn(email: String, password: String, completion: (Boolean, String?) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    user = firebaseAuth.currentUser
                    completion(task.isSuccessful, null)
                } else {
                    val errorMessage = task.exception?.let { extractSignInErrorMessage(it) }
                    completion(task.isSuccessful, errorMessage)
                }
            }
    }

    override fun signInWithGoogle(account: GoogleSignInAccount, completion: (Boolean) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            completion(task.isSuccessful)
        }
    }

    override fun sendEmailVerification(completion: (Boolean) -> Unit) {
        user?.sendEmailVerification()?.addOnCompleteListener { task ->
            completion(task.isSuccessful)
        }
    }

    private fun isAuthenticated(): Boolean = user != null

    private fun getCurrentUser(): FirebaseUser? = user

    private fun extractSignInErrorMessage(exception: Exception): String {
        return when (exception) {
            is FirebaseAuthWeakPasswordException -> "Weak password"
            is FirebaseAuthInvalidUserException -> "Account not found"
            is FirebaseAuthInvalidCredentialsException -> "Invalid credentials"
            is FirebaseAuthUserCollisionException -> "Email already in use"
            else -> "Unknown error"
        }
    }
}