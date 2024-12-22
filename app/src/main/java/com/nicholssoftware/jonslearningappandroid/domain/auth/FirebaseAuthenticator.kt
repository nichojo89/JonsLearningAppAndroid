package com.nicholssoftware.jonslearningappandroid.domain.auth

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.FirebaseError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.nicholssoftware.jonslearningappandroid.domain.preferences.PreferencesDataSource
import java.lang.Exception
import javax.inject.Inject

class FirebaseAuthenticator @Inject constructor(
    private val preferencesDataSourceImpl : PreferencesDataSource
) : Authenticator {
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

    override fun signIn(email: String, password: String, rememberUser: Boolean, completion: (Boolean, String?) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    user = firebaseAuth.currentUser
                    getFederatedToken { success, token ->
                        if(success){
                            preferencesDataSourceImpl.setOAuthToken(token.toString())
                            preferencesDataSourceImpl.setSignedIn(rememberUser)
                            completion(task.isSuccessful, null)
                        } else {
                            completion(task.isSuccessful, "Sign in failed")
                        }
                    }

                } else {
                    val errorMessage = task.exception?.let { extractSignInErrorMessage(it) }
                    completion(task.isSuccessful, errorMessage)
                }
            }
    }

    override fun signInWithGoogle(account: GoogleSignInAccount, rememberUser: Boolean, completion: (Boolean) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if(task.isSuccessful){
                user = task.result.user
                getFederatedToken{ success,token ->
                    if(success){
                        preferencesDataSourceImpl.setOAuthToken(token.toString())
                        preferencesDataSourceImpl.setSignedIn(rememberUser)
                        completion(true)
                    } else {
                        completion(false)
                    }
                }
            } else {
                completion(false)
            }

        }
    }

    override fun sendEmailVerification(completion: (Boolean) -> Unit) {
        user?.sendEmailVerification()?.addOnCompleteListener { task ->
            completion(task.isSuccessful)
        }
    }

    override fun userCanSignIn(completion: (Boolean) -> Unit) {
        if (user == null) {
            completion(false)
            return
        }
        val rememberSignIn = preferencesDataSourceImpl.isSignedIn()
        user?.let {
            it.getIdToken(true).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val idToken = task.result?.token
                    if (idToken != null && rememberSignIn && it.isEmailVerified) {
                        completion(true)
                    } else {
                        completion(false)
                    }
                } else {
                    completion(false)
                }
            }
        }
    }

    override fun sendPasswordResetEmail(email: String, onResult: (Boolean) -> Unit) {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                onResult(true)
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

    private fun getFederatedToken(completion: (Boolean, String?) -> Unit){
        user?.let {
            it.getIdToken(true)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Token is available
                        val token = task.result?.token
                        completion(true, token)
                    } else {
                        completion(false, null)
                    }
                }
        }
    }
}