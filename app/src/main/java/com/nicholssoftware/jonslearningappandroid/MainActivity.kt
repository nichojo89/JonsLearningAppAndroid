package com.nicholssoftware.jonslearningappandroid

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.nicholssoftware.jonslearningappandroid.navigation.AppNavigation
import com.nicholssoftware.jonslearningappandroid.ui.theme.JonsLearningAppAndroidTheme
import dagger.hilt.android.AndroidEntryPoint

private const val GOOGLE_REQ_ID = 600613

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val isLoggedIn = false
    private var signIntoGoogle: ((GoogleSignInAccount) -> Unit) = {}
    private lateinit var navController: NavHostController
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.google_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        setContent {
            navController = rememberNavController()
            JonsLearningAppAndroidTheme {
                AppNavigation(
                    isLoggedIn = isLoggedIn,
                    navController = navController,
                    requestSignInWithGoogle = { requestSignInWithGoogle() },
                    signIntoGoogle = { signIn ->
                        signIntoGoogle = signIn
                    }
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_REQ_ID) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInRequest(task)
        }
    }

    private fun requestSignInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, GOOGLE_REQ_ID)
    }

    private fun handleGoogleSignInRequest(task: com.google.android.gms.tasks.Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            account?.let {
                val firebaseAuth = FirebaseAuth.getInstance()
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Now get the Firebase ID token
                            firebaseAuth.currentUser?.getIdToken(true)?.addOnSuccessListener { idToken ->
                                // Send this Firebase ID token to the backend
                                val token = idToken.token
                            }
                        } else {
                            Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        } catch (e: ApiException) {
            Toast.makeText(this, "${getString(R.string.google_sign_in_failed)}: ${e.localizedMessage}", Toast.LENGTH_SHORT)
                .show()
        }
    }
}