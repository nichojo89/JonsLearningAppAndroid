package com.nicholssoftware.jonslearningappandroid

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.nicholssoftware.jonslearningappandroid.navigation.NavigationConstants
import com.nicholssoftware.jonslearningappandroid.ui.dashboard.DashboardScreen
import com.nicholssoftware.jonslearningappandroid.ui.login.sign_in.SignInScreen
import com.nicholssoftware.jonslearningappandroid.ui.login.sign_in.SignInViewModel
import com.nicholssoftware.jonslearningappandroid.ui.login.sign_up.SignUpScreen
import com.nicholssoftware.jonslearningappandroid.ui.login.sign_up.SignUpViewModel
import com.nicholssoftware.jonslearningappandroid.ui.theme.JonsLearningAppAndroidTheme

class MainActivity : ComponentActivity() {
    val isLoggedIn = false
    private val signInViewModel: SignInViewModel by viewModels()
    private val signUpViewModel: SignUpViewModel by viewModels()
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var navController : NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.google_web_client_id))  // Ensure you have your Web client ID
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        setContent {
            navController = rememberNavController()
            JonsLearningAppAndroidTheme {
                val startScreen = if(isLoggedIn) "dashboard" else "signin"
                NavHost(navController = navController, startDestination = startScreen) {
                    composable(NavigationConstants.SIGNIN) {
                        SignInScreen(
                            resetNavigation = signInViewModel::resetNavigation,
                            navigationEvent = signInViewModel.navigationEvent,
                            navController = navController,
                            usernameFlow = signInViewModel.usernameFlow,
                            passwordFlow = signInViewModel.passwordFlow,
                            updateUsername = signInViewModel::updateUsername,
                            updatePassword = signInViewModel::updatePassword,
                            sendForgotPassword = signInViewModel::sendForgotPassword,
                            signInWithGoogle = {signInWithGoogle()},
                            createAccount = signInViewModel::createAccount,
                            validateCredentials = signInViewModel::validateCredentials,
                            usernameErrorMessage = signInViewModel.usernameErrorMessage,
                            signInEnabled = signInViewModel.signInEnabled,
                            signIn = signInViewModel::signIn,
                            passwordErrorMessage = signInViewModel.usernameErrorMessage
                        )
                    }
                    composable(NavigationConstants.SIGNUP){
                        SignUpScreen(
                            resetNavigation = signUpViewModel::resetNavigation,
                            navigationEvent = signUpViewModel.navigationEvent,
                            navController = navController,
                            usernameFlow = signUpViewModel.usernameFlow,
                            passwordFlow = signUpViewModel.passwordFlow,
                            updateUsername = signUpViewModel::updateUsername,
                            updatePassword = signUpViewModel::updatePassword,
                            signUpWithGoogle = {signInWithGoogle()},
                            validateCredentials = signUpViewModel::validateCredentials,
                            usernameErrorMessage = signUpViewModel.usernameErrorMessage,
                            signInEnabled = signUpViewModel.signUpEnabled,
                            signUp = signUpViewModel::signUp,
                            confirmPasswordFlow = signUpViewModel.confirmPasswordFlow,
                            updateConfirmPasswordFlow = signUpViewModel::updateConfirmPassword,
                            isConfirmPasswordVisible = signUpViewModel.isConfirmPasswordVisibile
                        )
                    }

                    composable(NavigationConstants.DASHBOARD) {
                        DashboardScreen()
                    }
                }
            }
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, 123)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task)
        }
    }

    private fun handleGoogleSignInResult(task: com.google.android.gms.tasks.Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            account?.let {
                navController.currentDestination?.route.toString().let { route ->
                    when(route){
                        NavigationConstants.SIGNIN -> signInViewModel.signInWithGoogle(account)
                        else -> signUpViewModel.signUpWithGoogle(account)
                    }
                }
            }
        } catch (e: ApiException) {
            Toast.makeText(this, "Google Sign-In Failed: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }
}