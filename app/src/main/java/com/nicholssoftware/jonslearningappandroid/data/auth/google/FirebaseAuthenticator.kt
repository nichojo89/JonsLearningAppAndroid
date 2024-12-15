package com.nicholssoftware.jonslearningappandroid.data.auth.google

import android.content.Context
import android.util.Log
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.nicholssoftware.jonslearningappandroid.navigation.NavigationConstants

object FirebaseAuthenticator {
    private var auth: FirebaseAuth
    private var user: FirebaseUser? = null
    private val tag = "Firebase Auth"
    private val clientId: String = ""
    val firebaseAuth = FirebaseAuth.getInstance()

    init{
        auth = Firebase.auth
    }

    fun signUp(email: String, password: String, completion: (Boolean) -> Unit){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(tag, "createUserWithEmail:success")
                    user = auth.currentUser
                } else {
                    Log.w(tag, "createUserWithEmail:failure", task.exception)
                }
                completion(task.isSuccessful)
            }
    }

    fun signIn(email: String, password: String, completion: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(tag, "signInWithEmail:success")
                    user = auth.currentUser
                } else {
                    Log.w(tag, "signInWithEmail:failure", task.exception)
                }
                completion(task.isSuccessful)
            }
    }

    fun signInWithGoogle(account: GoogleSignInAccount, completion: (Boolean) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            completion(task.isSuccessful)
        }
    }
}