package com.nicholssoftware.jonslearningappandroid.ui.login.email_verification

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun EmailVerificationScreen(
    showDialog: State<Boolean>,
    dialogMessage: State<String>,
    updateShowDialog: (Boolean) -> Unit,
    updateDialogMessage: (String) -> Unit,
    sendVerificationEmail: ((Boolean) -> Unit) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center){
        OutlinedButton(
            onClick = {
                sendVerificationEmail{ success ->
                    val message = if(success) "Email verification sent" else "Email verification failed"
                    updateDialogMessage(message)
                    updateShowDialog(true)
                }
            }
        ) {
            Text("Send verification email")
        }
    }
    if(showDialog.value) {
        AlertDialog(
            onDismissRequest = {
                updateShowDialog(false)
            },
            title = {
                Text("Email Verification")
            },
            text = {
                Text(dialogMessage.value)
            },
            confirmButton = {
                Button(onClick = {
                    updateShowDialog(false)
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(onClick = {
                    updateShowDialog(false)
                }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EmailVerificationScreenPreview() {
    val showDialog = remember { mutableStateOf(false) }
    val dialogMessage = remember { mutableStateOf("") }
    val updateShowDialog: (Boolean) -> Unit = { showDialog.value = it }
    val updateDialogMessage: (String) -> Unit = { dialogMessage.value = it }
    val updateEmailVerificationSuccess: (Boolean) -> Unit = {}
    val sendVerificationEmail: ((Boolean) -> Unit) -> Unit = { callback ->
        callback(true)
    }

    // Call the composable with mock data
    EmailVerificationScreen(
        showDialog = showDialog,
        dialogMessage = dialogMessage,
        updateShowDialog = updateShowDialog,
        updateDialogMessage = updateDialogMessage,
        sendVerificationEmail = sendVerificationEmail
    )
}