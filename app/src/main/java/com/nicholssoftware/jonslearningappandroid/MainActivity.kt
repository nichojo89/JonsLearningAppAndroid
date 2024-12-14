package com.nicholssoftware.jonslearningappandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nicholssoftware.jonslearningappandroid.navigation.NavigationConstants
import com.nicholssoftware.jonslearningappandroid.ui.dashboard.DashboardScreen
import com.nicholssoftware.jonslearningappandroid.ui.login.sign_in.SignInScreen
import com.nicholssoftware.jonslearningappandroid.ui.login.sign_in.SignInViewModel
import com.nicholssoftware.jonslearningappandroid.ui.login.sign_up.SignUpScreen
import com.nicholssoftware.jonslearningappandroid.ui.theme.JonsLearningAppAndroidTheme

class MainActivity : ComponentActivity() {
    val isLoggedIn = false
    private val signInViewModel: SignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
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
                            signInWithGoogle = signInViewModel::signInWithGoogle,
                            createAccount = signInViewModel::createAccount,
                            validateCredentials = signInViewModel::validateCredentials,
                            usernameErrorMessage = signInViewModel.usernameErrorMessage,
                            signInEnabled = signInViewModel.signInEnabled,
                            signIn = signInViewModel::signIn
                        )
                    }
                    composable(NavigationConstants.SIGNUP){
                        SignUpScreen(
                            resetNavigation = signInViewModel::resetNavigation,
                            navigationEvent = signInViewModel.navigationEvent,
                            navController = navController,
                            usernameFlow = signInViewModel.usernameFlow,
                            passwordFlow = signInViewModel.passwordFlow,
                            updateUsername = signInViewModel::updateUsername,
                            updatePassword = signInViewModel::updatePassword,
                            signInWithGoogle = signInViewModel::signInWithGoogle,
                            createAccount = signInViewModel::createAccount,
                            validateCredentials = signInViewModel::validateCredentials,
                            usernameErrorMessage = signInViewModel.usernameErrorMessage,
                            signInEnabled = signInViewModel.signInEnabled,
                            signIn = signInViewModel::signIn
                        )
                    }

                    composable(NavigationConstants.DASHBOARD) {
                        DashboardScreen()
                    }
                }
            }
        }
    }
}