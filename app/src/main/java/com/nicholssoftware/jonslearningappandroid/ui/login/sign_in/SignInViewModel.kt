package com.nicholssoftware.jonslearningappandroid.ui.login.sign_in

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.nicholssoftware.jonslearningappandroid.navigation.NavigationConstants
import com.nicholssoftware.jonslearningappandroid.ui.BaseViewModel
import com.nicholssoftware.jonslearningappandroid.util.stringutil.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor() : BaseViewModel() {
    private val _usernameFlow = mutableStateOf("")
    val usernameFlow : State<String> = _usernameFlow

    private val _passwordFlow = mutableStateOf("")
    val passwordFlow : State<String> = _passwordFlow

    private val _signInEnabled = mutableStateOf(false)
    val signInEnabled : State<Boolean> = _signInEnabled

    private val _usernameErrorMessage = mutableStateOf("")
    val usernameErrorMessage : State<String> = _usernameErrorMessage

    fun updateUsername(username: String){
        _usernameFlow.value = username
    }
    fun updatePassword(password: String){
        _passwordFlow.value = password
    }

    private fun updateSignInEnabled(value: Boolean) {
        _signInEnabled.value = value
    }

    fun sendForgotPassword(){

    }

    fun signInWithGoogle(){

    }

    fun createAccount(){
        updateNavigationEvent(NavigationConstants.SIGNUP)
    }

    fun signIn(){
        if(signInEnabled.value){
            updateNavigationEvent(NavigationConstants.DASHBOARD)
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