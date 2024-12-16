package com.nicholssoftware.jonslearningappandroid.data.auth.google

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import java.lang.Exception
import javax.inject.Inject

class FirebaseAuthenticator @Inject constructor() {
    private var auth: FirebaseAuth
    private var user: FirebaseUser? = null
    private val tag = "Firebase Auth"
    private val clientId: String = ""
    val firebaseAuth = FirebaseAuth.getInstance()

    init{
        auth = Firebase.auth
    }

    fun signUp(email: String, password: String, completion: (Boolean, String?) -> Unit){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    user = auth.currentUser
                    completion(task.isSuccessful, null)
                } else {
                    val errorMessage = task.exception?.let {
                        extractSignInErrorMessage(it)
                    }
                    completion(task.isSuccessful, errorMessage)
                }
            }
    }

    fun signIn(email: String, password: String, completion: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    user = auth.currentUser
                    completion(task.isSuccessful, null)
                } else {
                    val errorMessage = task.exception?.let {
                        extractSignInErrorMessage(it)
                    }
                    completion(task.isSuccessful, errorMessage)
                }
            }
    }

    fun extractSignInErrorMessage(exception: Exception) : String {
        return when (exception) {
            is FirebaseAuthWeakPasswordException -> "Weak password"
            is FirebaseAuthInvalidUserException -> "Account not found"
            is FirebaseAuthInvalidCredentialsException -> "Invalid credentials"
            else -> "Unknown error"
        }
    }

    fun signInWithGoogle(account: GoogleSignInAccount, completion: (Boolean) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            completion(task.isSuccessful)
        }
    }

    fun sendEmailVerification(completion: (Boolean) -> Unit) {
        user?.let {
            it.sendEmailVerification()
                .addOnCompleteListener { task ->
                    completion(task.isSuccessful)
                }
        }
    }
}