package com.nicholssoftware.jonslearningappandroid.data.auth.google

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import java.lang.Exception
import javax.inject.Inject

class FirebaseAuthenticator @Inject constructor() {
    val firebaseAuth = FirebaseAuth.getInstance()
    private var auth: FirebaseAuth = Firebase.auth
    companion object {
        var user: FirebaseUser? = null
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
                    getToken(completion)
                } else {
                    val errorMessage = task.exception?.let {
                        extractSignInErrorMessage(it)
                    }
                    completion(task.isSuccessful, errorMessage)
                }
            }
    }

    fun getToken(completion: (Boolean, String?) -> Unit) {
        user?.let {
            it.getIdToken(true)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val idToken = task.result?.token
                        println("ID Token: $idToken")
                    } else {
                        // Handle error
                        task.exception?.let { exception ->
                            println("Error getting token: ${exception.localizedMessage}")
                        }
                    }
                    completion(task.isSuccessful, null)
                }
        }
        user
    }

    fun extractSignInErrorMessage(exception: Exception) : String {
        return when (exception) {
            is FirebaseAuthWeakPasswordException -> "Weak password"
            is FirebaseAuthInvalidUserException -> "Account not found"
            is FirebaseAuthInvalidCredentialsException -> "Invalid credentials"
            is FirebaseAuthUserCollisionException -> "Email already in use"
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