package com.nicholssoftware.jonslearningappandroid.ui.sign_up

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.nicholssoftware.jonslearningappandroid.domain.auth.SignInWithGoogleUseCase
import com.nicholssoftware.jonslearningappandroid.domain.auth.SignUpUseCase
import com.nicholssoftware.jonslearningappandroid.navigation.NavigationConstants
import com.nicholssoftware.jonslearningappandroid.ui.BaseViewModel
import com.nicholssoftware.jonslearningappandroid.util.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase
) : BaseViewModel() {
    private val _usernameFlow = mutableStateOf("")
    val usernameFlow : State<String> = _usernameFlow

    private val _passwordFlow = mutableStateOf("")
    val passwordFlow : State<String> = _passwordFlow

    private val _signUpEnabled = mutableStateOf(false)
    val signUpEnabled : State<Boolean> = _signUpEnabled

    private val _confirmPasswordFlow = mutableStateOf("")
    val confirmPasswordFlow : State<String> = _confirmPasswordFlow

    private val _usernameErrorMessage = mutableStateOf("")
    val usernameErrorMessage : State<String> = _usernameErrorMessage

    private val _passwordErrorMessage = mutableStateOf("")
    val passwordErrorMessage : State<String> = _passwordErrorMessage

    private val _isConfirmPasswordVisible = mutableStateOf(false)
    val isConfirmPasswordVisibile : State<Boolean> = _isConfirmPasswordVisible

    fun updateUsername(username: String) {
        _usernameFlow.value = username
    }

    fun updatePassword(password: String){
        _passwordFlow.value = password
        _isConfirmPasswordVisible.value = password.isNotEmpty()
    }

    fun updateConfirmPassword(password: String) {
        _confirmPasswordFlow.value = password
    }

    fun updateSignUpEnabled(enabled: Boolean) {
        _signUpEnabled.value = enabled
    }

    fun signUp() {
        updateSignUpEnabled(false)
        if(_passwordFlow.value.isNotEmpty() && _passwordFlow.value == _confirmPasswordFlow.value){
            signUpUseCase(usernameFlow.value,passwordFlow.value){ success, error ->
                if(success){
                    updateNavigationEvent(NavigationConstants.EMAIL_VERIFICATION)
                } else {
                    _passwordErrorMessage.value = error.toString()
                }
                updateSignUpEnabled(true)
            }
        } else {
            _passwordErrorMessage.value = "Passwords do not match"
        }
    }

    fun validateCredentials(){
        _usernameErrorMessage.value = if(_usernameFlow.value.isNotEmpty() && !_usernameFlow.value.isValidEmail() && _passwordFlow.value.isNotEmpty()) {
            "invalid username"
        } else {
            ""
        }

        if(_usernameFlow.value.isValidEmail()
            && _passwordFlow.value.isNotEmpty()
            && _confirmPasswordFlow.value.isNotEmpty()
            && _passwordFlow.value == _confirmPasswordFlow.value){
            updateSignUpEnabled(true)
        } else {
            updateSignUpEnabled(false)
        }
    }

    fun signUpWithGoogle(account: GoogleSignInAccount, navController: NavController) {
        navController.currentDestination?.route.toString().let { route ->
            if(route == NavigationConstants.SIGNUP){
                signInWithGoogleUseCase(account, true ){success ->
                    if (success) {
                        updateNavigationEvent(NavigationConstants.DASHBOARD)
                    } else {
                        _passwordErrorMessage.value = "Google sign-up failed"
                    }
                }
            }
        }
    }
}