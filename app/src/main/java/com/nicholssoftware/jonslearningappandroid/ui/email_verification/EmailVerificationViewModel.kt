package com.nicholssoftware.jonslearningappandroid.ui.email_verification

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.nicholssoftware.jonslearningappandroid.R
import com.nicholssoftware.jonslearningappandroid.domain.auth.FirebaseAuthenticator
import com.nicholssoftware.jonslearningappandroid.domain.auth.SendEmailVerificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class EmailVerificationViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val sendEmailVerificationUseCase: SendEmailVerificationUseCase
) : ViewModel() {
    private val _isSendEmailEnabled = mutableStateOf(true)
    val isSendEmailEnabled: State<Boolean> = _isSendEmailEnabled

    private val _sendText = mutableStateOf(context.getText(R.string.send).toString())
    val sendText :State<String> = _sendText

    fun sendVerificationEmail(completion: (Boolean) -> Unit){
        _isSendEmailEnabled.value = false
        sendEmailVerificationUseCase {success ->
            if(success){
                completion(true)
                _isSendEmailEnabled.value = true
            }
        }
    }
}