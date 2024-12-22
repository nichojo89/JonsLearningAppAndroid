package com.nicholssoftware.jonslearningappandroid.ui.sign_in

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.nicholssoftware.jonslearningappandroid.domain.auth.FirebaseAuthenticator
import com.nicholssoftware.jonslearningappandroid.domain.auth.PasswordResetUseCase
import com.nicholssoftware.jonslearningappandroid.domain.auth.SignInRequiredUseCase
import com.nicholssoftware.jonslearningappandroid.domain.auth.SignInUseCase
import com.nicholssoftware.jonslearningappandroid.domain.auth.SignInWithGoogleUseCase
import com.nicholssoftware.jonslearningappandroid.domain.preferences.PreferencesDataSource
import com.nicholssoftware.jonslearningappandroid.navigation.NavigationConstants
import com.nicholssoftware.jonslearningappandroid.ui.BaseViewModel
import com.nicholssoftware.jonslearningappandroid.util.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    @ApplicationContext private val context: Context,
    private val passwordResetUseCase: PasswordResetUseCase,
    private val userSignedInUseCase: SignInRequiredUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val preferencesDataSourceImpl : PreferencesDataSource
) : BaseViewModel() {
    private val _userSignedIn = MutableStateFlow<Boolean?>(null)
    val userSignedIn: StateFlow<Boolean?> = _userSignedIn.asStateFlow()

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

    private val _rememberUser = mutableStateOf(true)
    val rememberUser : State<Boolean> = _rememberUser

    private var _navController: NavController? = null

    init {
        checkSignInRequired()
    }

    fun updateUsername(username: String){
        _usernameFlow.value = username
    }
    fun updatePassword(password: String){
        _passwordFlow.value = password
    }

    fun updatePasswordErrorMessage(error: String){
        _passwordErrorMessage.value = error
    }

    fun updateSignInEnabled(signInEnabled: Boolean) {
        _signInEnabled.value = signInEnabled
    }

    fun updateNavController(navController: NavController){
        _navController = navController
    }

    private fun checkSignInRequired() {
        viewModelScope.launch {
            userSignedInUseCase.invoke {
                _userSignedIn.value = it
            }
        }
    }

    fun updateRememberUser(remember:Boolean){
        _rememberUser.value = remember
    }

    fun createAccount(){
        updateNavigationEvent(NavigationConstants.SIGNUP)
    }

    fun sendForgotPassword(){
        if(_usernameFlow.value.isValidEmail()){
            passwordResetUseCase.invoke(_usernameFlow.value){ success ->
                val message = if(success){
                    "Password reset email sent"
                } else {
                    "Reset failed"
                }
                Toast.makeText(context,message, Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context,"Invalid email", Toast.LENGTH_SHORT).show()
        }
    }

    fun signIn(){
        if(signInEnabled.value){
            updateSignInEnabled(false)
            signInUseCase(usernameFlow.value, passwordFlow.value, rememberUser.value){ success, error ->
                if(success){
                    FirebaseAuthenticator.user?.let { user ->
                        if(user.isEmailVerified) {
                            navigateToDashboard(_navController)
                        } else {
                            updateNavigationEvent(NavigationConstants.EMAIL_VERIFICATION)
                        }
                    }
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
                signInWithGoogleUseCase(account, rememberUser.value){success ->
                    if (success) {
                        preferencesDataSourceImpl.setSignedIn(_rememberUser.value)
                        navigateToDashboard(navController)
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

    private fun navigateToDashboard(navController: NavController?){
        navController?.let {
            it.navigate(NavigationConstants.DASHBOARD){
                popUpTo(NavigationConstants.DASHBOARD) { inclusive = true }
            }
        }
    }
}