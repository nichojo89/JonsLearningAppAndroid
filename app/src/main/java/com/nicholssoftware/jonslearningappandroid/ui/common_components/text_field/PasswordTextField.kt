package com.nicholssoftware.jonslearningappandroid.ui.common_components.text_field

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.nicholssoftware.jonslearningappandroid.ui.common_components.font.FontAwesomeIcon

@Composable
fun PasswordTextField(
    label: String,
    signIn : () -> Unit,
    modifier: Modifier,
    password: String,
    onPasswordChange: (String) -> Unit,
    onTrailingIconClick: () -> Unit,
    hidePassword: Boolean,
    errorMessage: String = ""
) {
    val trailingIcon =
        if (hidePassword) FontAwesomeIcon.EYESLASH
        else FontAwesomeIcon.EYE

    val visualTransformation =
        if (hidePassword) PasswordVisualTransformation()
        else VisualTransformation.None

    CustomTextField(
        onDone = signIn,
        modifier = modifier,
        value = password,
        errorMessage = errorMessage,
        onValueChange = onPasswordChange,
        leadingIcon = FontAwesomeIcon.LOCK.unicode,
        trailingIcon = trailingIcon.unicode,
        onTrailingIconClick = onTrailingIconClick,
        label = label,
        visualTransformation = visualTransformation,
    )
}