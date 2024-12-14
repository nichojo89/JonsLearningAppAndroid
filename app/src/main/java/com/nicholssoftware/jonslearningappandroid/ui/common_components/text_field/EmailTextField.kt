package com.nicholssoftware.jonslearningappandroid.ui.common_components.text_field

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nicholssoftware.jonslearningappandroid.ui.common_components.font.FontAwesomeIcon

@Composable
fun EmailTextField(
    signIn : () -> Unit,
    modifier: Modifier,
    email: String,
    onEmailChange: (String) -> Unit,
    errorMessage: String = ""
) {
    CustomTextField(
        onDone = signIn,
        value = email,
        label = "Username",
        errorMessage = errorMessage,
        onValueChange = onEmailChange,
        modifier = modifier,
        leadingIcon = FontAwesomeIcon.ENVELOPE.unicode
    )
}