package com.nicholssoftware.jonslearningappandroid.ui.login.sign_in

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.nicholssoftware.jonslearningappandroid.data.auth.google.FirebaseAuthenticator
import com.nicholssoftware.jonslearningappandroid.navigation.NavigationConstants
import com.nicholssoftware.jonslearningappandroid.ui.BaseViewModel
import com.nicholssoftware.jonslearningappandroid.util.stringutil.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val firebaseAuthenticator: FirebaseAuthenticator
) : BaseViewModel() {
    private val _usernameFlow = mutableStateOf("")
    val usernameFlow : State<String> = _usernameFlow

    private val _passwordFlow = mutableStateOf("")
    val passwordFlow : State<String> = _passwordFlow

    private val _signInEnabled = mutableStateOf(false)
    val signInEnabled : State<Boolean> = _signInEnabled

    private val _usernameErrorMessage = mutableStateOf("")
    val usernameErrorMessage : State<String> = _usernameErrorMessage

    private val _passwordErrorMessage = mutableStateOf("")
    val passwordErrorMessage : State<String> = _passwordErrorMessage

    fun updateUsername(username: String){
        _usernameFlow.value = username
    }
    fun updatePassword(password: String){
        _passwordFlow.value = password
    }

    fun updatePasswordErrorMessage(error: String){
        _passwordErrorMessage.value = error
    }

    fun updateSignInEnabled(value: Boolean) {
        _signInEnabled.value = value
    }

    fun sendForgotPassword(){

    }

    fun createAccount(){
        updateNavigationEvent(NavigationConstants.SIGNUP)
    }

    fun signIn(){
        if(signInEnabled.value){
            updateSignInEnabled(false)
            firebaseAuthenticator.signIn(usernameFlow.value, passwordFlow.value){ success, error ->
                if(success){
                    updateNavigationEvent(NavigationConstants.DASHBOARD)
                } else {
                    error?.let {
                        updatePasswordErrorMessage(it)
                    }
                }
                validateCredentials()
            }
        }
    }

    fun signInWithGoogle(account: GoogleSignInAccount, navController: NavController) {
        navController.currentDestination?.route.toString().let { route ->
            if(route == NavigationConstants.SIGN_IN){
                firebaseAuthenticator.signInWithGoogle(account){success ->
                    if (success) {
                        updateNavigationEvent(NavigationConstants.DASHBOARD)  // Navigate to dashboard on success
                    } else {
                        _passwordErrorMessage.value = "Google sign-in failed"
                    }
                }
            }
        }
    }

    fun validateCredentials(){
        _usernameErrorMessage.value = if(!_usernameFlow.value.isValidEmail() && _passwordFlow.value.isNotEmpty()) {
            "invalid username"
        } else {
            ""
        }
        if(_usernameFlow.value.isValidEmail() && _passwordFlow.value.isNotEmpty()){
            updateSignInEnabled(true)
        } else {
            updateSignInEnabled(false)
        }
    }
}