package com.nicholssoftware.jonslearningappandroid.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nicholssoftware.jonslearningappandroid.ui.dashboard.DashboardScreen
import com.nicholssoftware.jonslearningappandroid.ui.login.email_verification.EmailVerificationScreen
import com.nicholssoftware.jonslearningappandroid.ui.login.email_verification.EmailVerificationViewModel
import com.nicholssoftware.jonslearningappandroid.ui.login.sign_in.SignInScreen
import com.nicholssoftware.jonslearningappandroid.ui.login.sign_in.SignInViewModel
import com.nicholssoftware.jonslearningappandroid.ui.login.sign_up.SignUpScreen
import com.nicholssoftware.jonslearningappandroid.ui.login.sign_up.SignUpViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    isLoggedIn: Boolean,
    signInViewModel: SignInViewModel,
    signUpViewModel: SignUpViewModel,
    emailVerificationViewModel: EmailVerificationViewModel,
    signInWithGoogle: () -> Unit
) {
    val startScreen = if (isLoggedIn) NavigationConstants.DASHBOARD else NavigationConstants.SIGN_IN

    NavHost(navController = navController, startDestination = startScreen) {
        composable(NavigationConstants.SIGN_IN) {
            SignInScreen(
                resetNavigation = signInViewModel::resetNavigation,
                navigationEvent = signInViewModel.navigationEvent,
                navController = navController,
                usernameFlow = signInViewModel.usernameFlow,
                passwordFlow = signInViewModel.passwordFlow,
                updateUsername = signInViewModel::updateUsername,
                updatePassword = signInViewModel::updatePassword,
                sendForgotPassword = signInViewModel::sendForgotPassword,
                signInWithGoogle = { signInWithGoogle() },
                createAccount = signInViewModel::createAccount,
                validateCredentials = signInViewModel::validateCredentials,
                usernameErrorMessage = signInViewModel.usernameErrorMessage,
                signInEnabled = signInViewModel.signInEnabled,
                signIn = signInViewModel::signIn,
                passwordErrorMessage = signInViewModel.usernameErrorMessage
            )
        }
        composable(NavigationConstants.SIGNUP) {
            SignUpScreen(
                resetNavigation = signUpViewModel::resetNavigation,
                navigationEvent = signUpViewModel.navigationEvent,
                navController = navController,
                usernameFlow = signUpViewModel.usernameFlow,
                passwordFlow = signUpViewModel.passwordFlow,
                updateUsername = signUpViewModel::updateUsername,
                updatePassword = signUpViewModel::updatePassword,
                signUpWithGoogle = { signInWithGoogle() },
                validateCredentials = signUpViewModel::validateCredentials,
                usernameErrorMessage = signUpViewModel.usernameErrorMessage,
                signInEnabled = signUpViewModel.signUpEnabled,
                signUp = signUpViewModel::signUp,
                confirmPasswordFlow = signUpViewModel.confirmPasswordFlow,
                updateConfirmPasswordFlow = signUpViewModel::updateConfirmPassword,
                isConfirmPasswordVisible = signUpViewModel.isConfirmPasswordVisibile,
                passwordErrorMessage = signUpViewModel.usernameErrorMessage
            )
        }
        composable(NavigationConstants.EMAIL_VERIFICATION) {
            EmailVerificationScreen(
                showDialog = emailVerificationViewModel.showDialog,
                dialogMessage = emailVerificationViewModel.dialogMessage,
                updateShowDialog = emailVerificationViewModel::updateShowDialog,
                updateDialogMessage = emailVerificationViewModel::updateDialogMessage,
                sendVerificationEmail = emailVerificationViewModel::sendVerificationEmail
            )
        }
        composable(NavigationConstants.DASHBOARD) {
            DashboardScreen()
        }
    }
}