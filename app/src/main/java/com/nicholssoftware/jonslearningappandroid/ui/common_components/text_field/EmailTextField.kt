package com.nicholssoftware.jonslearningappandroid.ui.common_components.text_field

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nicholssoftware.jonslearningappandroid.ui.common_components.font.FontAwesomeIcon

@Composable
fun EmailTextField(
    label: String,
    signIn : () -> Unit,
    modifier: Modifier,
    email: String,
    onEmailChange: (String) -> Unit,
    errorMessage: String = ""
) {
    CustomTextField(
        label = label,
        value = email,
        onDone = signIn,
        modifier = modifier,
        errorMessage = errorMessage,
        onValueChange = onEmailChange,
        leadingIcon = FontAwesomeIcon.ENVELOPE.unicode
    )
}