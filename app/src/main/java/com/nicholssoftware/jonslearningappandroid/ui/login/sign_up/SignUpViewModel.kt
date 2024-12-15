package com.nicholssoftware.jonslearningappandroid.ui.login.sign_up

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.nicholssoftware.jonslearningappandroid.ui.BaseViewModel
import com.nicholssoftware.jonslearningappandroid.util.stringutil.isValidEmail
import javax.inject.Inject

class SignUpViewModel @Inject constructor() : BaseViewModel() {
    private val _usernameFlow = mutableStateOf("")
    val usernameFlow : State<String> = _usernameFlow

    private val _passwordFlow = mutableStateOf("")
    val passwordFlow : State<String> = _passwordFlow

    private val _confirmPasswordFlow = mutableStateOf("")
    val confirmPasswordFlow : State<String> = _confirmPasswordFlow

    private val _usernameErrorMessage = mutableStateOf("")
    val usernameErrorMessage : State<String> = _usernameErrorMessage

    private val _signUpEnabled = mutableStateOf(false)
    val signUpEnabled : State<Boolean> = _signUpEnabled

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

    fun signUpWithGoogle() {

    }
    fun signUp() {

    }

    fun createAccount() : Boolean {
        if(_passwordFlow.value.isNotEmpty() && _passwordFlow.value == _confirmPasswordFlow.value){
            _confirmPasswordFlow.value = ""
        } else {

        }
        return true
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
}