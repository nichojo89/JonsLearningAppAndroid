package com.nicholssoftware.jonslearningappandroid.domain.auth.repository

import android.util.Base64
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.nicholssoftware.jonslearningappandroid.domain.preferences.repository.PreferencesDataSource
import org.json.JSONObject
import java.lang.Exception
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

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

    override fun sendPasswordResetEmail(email: String, onResult: (Boolean) -> Unit) {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                onResult(true)
            }
    }

    override suspend fun getToken(): String? {
        // Retrieve the current token from preferences
        val currentToken = preferencesDataSourceImpl.getOAuthToken()

        // Check if the token is expired or null
        if (currentToken == null || isTokenExpired(currentToken)) {
            // Refresh the token if it's expired or null
            return refreshToken()
        }

        // Otherwise, return the valid token
        return currentToken
    }

    // Function to check if the token has expired
    private fun isTokenExpired(token: String): Boolean {
        val decodedToken = decodeJwt(token)
        val expirationTime = decodedToken?.get("exp") as? Long ?: return true
        // Convert expiration time from seconds to milliseconds and compare
        return expirationTime * 1000 < System.currentTimeMillis()
    }

    // Function to decode the JWT and extract information such as expiration time
    private fun decodeJwt(token: String): Map<String, Any>? {
        val parts = token.split(".")
        if (parts.size == 3) {
            val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
            val json = JSONObject(payload)
            val result = mutableMapOf<String, Any>()
            json.keys().forEach { key ->
                result[key] = json.get(key)
            }
            return result
        }
        return null
    }

    // Refresh the token if it's expired or missing
    private suspend fun refreshToken(): String? {
        val currentUser = firebaseAuth.currentUser ?: throw Exception("No authenticated user found.")

        return suspendCoroutine { continuation ->
            // Force refresh the token
            currentUser.getIdToken(true).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val newToken = task.result?.token
                    if (newToken != null) {
                        // Save the new token to preferences
                        preferencesDataSourceImpl.setOAuthToken(newToken)
                        continuation.resume(newToken)
                    } else {
                        continuation.resumeWithException(Exception("Failed to refresh token"))
                    }
                } else {
                    continuation.resumeWithException(task.exception ?: Exception("Failed to refresh token"))
                }
            }
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