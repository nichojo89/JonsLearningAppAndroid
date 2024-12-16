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
    private val _isSendEmailEnabled = mutableStateOf(true)
    val isSendEmailEnabled: State<Boolean> = _isSendEmailEnabled

    fun sendVerificationEmail(){
        firebaseAuthenticator.sendEmailVerification {success ->
//            completion(success)
        }
    }
}