package com.nicholssoftware.jonslearningappandroid.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.nicholssoftware.jonslearningappandroid.ui.dashboard.DashboardScreen
import com.nicholssoftware.jonslearningappandroid.ui.email_verification.EmailVerificationScreen
import com.nicholssoftware.jonslearningappandroid.ui.email_verification.EmailVerificationViewModel
import com.nicholssoftware.jonslearningappandroid.ui.sign_in.SignInScreen
import com.nicholssoftware.jonslearningappandroid.ui.sign_in.SignInViewModel
import com.nicholssoftware.jonslearningappandroid.ui.sign_up.SignUpScreen
import com.nicholssoftware.jonslearningappandroid.ui.sign_up.SignUpViewModel
private const val tweenSpeed = 300
@Composable
fun AppNavigation(
    isLoggedIn: Boolean,
    navController: NavHostController,
    requestSignInWithGoogle: () -> Unit,
    signIntoGoogle: (newImplementation: (GoogleSignInAccount) -> Unit) -> Unit
) {
    NavHost(navController = navController, startDestination = NavigationConstants.SIGN_IN) {
        composable(NavigationConstants.SIGN_IN,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(tweenSpeed)
                )
            }) {
            val signInViewModel: SignInViewModel = hiltViewModel()
            SignInScreen(
                navController = navController,
                signIntoGoogle = signIntoGoogle,
                signIn = signInViewModel::signIn,
                rememberUser = signInViewModel.rememberUser,
                usernameFlow = signInViewModel.usernameFlow,
                passwordFlow = signInViewModel.passwordFlow,
                signInEnabled = signInViewModel.signInEnabled,
                createAccount = signInViewModel::createAccount,
                updateUsername = signInViewModel::updateUsername,
                updatePassword = signInViewModel::updatePassword,
                navigationEvent = signInViewModel.navigationEvent,
                resetNavigation = signInViewModel::resetNavigation,
                userCanSignIn = signInViewModel.isSignInRequired,
                signInWithGoogle = signInViewModel::signInWithGoogle,
                requestSignInWithGoogle = { requestSignInWithGoogle() },
                sendForgotPassword = signInViewModel::sendForgotPassword,
                updateNavController = signInViewModel::updateNavController,
                validateCredentials = signInViewModel::validateCredentials,
                passwordErrorMessage = signInViewModel.passwordErrorMessage,
                usernameErrorMessage = signInViewModel.usernameErrorMessage,
            )
        }
        composable(NavigationConstants.SIGNUP,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(tweenSpeed)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(tweenSpeed)
                )
            }) {
            val signUpViewModel: SignUpViewModel = hiltViewModel()
            SignUpScreen(
                navController = navController,
                signIntoGoogle = signIntoGoogle,
                signUp = signUpViewModel::signUp,
                usernameFlow = signUpViewModel.usernameFlow,
                passwordFlow = signUpViewModel.passwordFlow,
                signInEnabled = signUpViewModel.signUpEnabled,
                updateUsername = signUpViewModel::updateUsername,
                updatePassword = signUpViewModel::updatePassword,
                navigationEvent = signUpViewModel.navigationEvent,
                resetNavigation = signUpViewModel::resetNavigation,
                signUpWithGoogle = signUpViewModel::signUpWithGoogle,
                requestSignInWithGoogle = { requestSignInWithGoogle() },
                confirmPasswordFlow = signUpViewModel.confirmPasswordFlow,
                validateCredentials = signUpViewModel::validateCredentials,
                usernameErrorMessage = signUpViewModel.usernameErrorMessage,
                passwordErrorMessage = signUpViewModel.passwordErrorMessage,
                updateConfirmPasswordFlow = signUpViewModel::updateConfirmPassword,
                isConfirmPasswordVisible = signUpViewModel.isConfirmPasswordVisibile,
            )
        }
        composable(NavigationConstants.EMAIL_VERIFICATION,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(tweenSpeed)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(tweenSpeed)
                )
            }) {
            val emailVerificationViewModel : EmailVerificationViewModel = hiltViewModel()
            EmailVerificationScreen(
                navController = navController,
                isSendEmailEnabled = emailVerificationViewModel.isSendEmailEnabled,
                sendVerificationEmail = emailVerificationViewModel::sendVerificationEmail,
                backButtonShouldPopStack = remember { mutableStateOf(true) }
            )
        }
        composable(NavigationConstants.DASHBOARD) {
            DashboardScreen()
        }
    }
}