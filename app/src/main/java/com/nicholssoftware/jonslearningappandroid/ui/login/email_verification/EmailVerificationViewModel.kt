package com.nicholssoftware.jonslearningappandroid.ui.login.email_verification

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.nicholssoftware.jonslearningappandroid.data.auth.google.FirebaseAuthenticator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EmailVerificationViewModel @Inject constructor(
    private val firebaseAuthenticator: FirebaseAuthenticator
) : ViewModel() {
    private val _showDialog = mutableStateOf(false)
    val showDialog : State<Boolean> = _showDialog

    private val _dialogMessage = mutableStateOf("")
    val dialogMessage : State<String> = _dialogMessage

    fun updateShowDialog(showDialog: Boolean){
        _showDialog.value = showDialog
    }

    fun updateDialogMessage(message: String){
        _dialogMessage.value = message
    }

    fun sendVerificationEmail(completion: (Boolean) -> Unit){
        firebaseAuthenticator.sendEmailVerification {success ->
            completion(success)
        }
    }
}