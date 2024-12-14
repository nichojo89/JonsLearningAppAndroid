package com.nicholssoftware.jonslearningappandroid.ui.common_components.text_field

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.nicholssoftware.jonslearningappandroid.R
import com.nicholssoftware.jonslearningappandroid.ui.common_components.font.FontAwesomeIcon

@Composable
fun PasswordTextField(
    signIn : () -> Unit,
    modifier: Modifier,
    password: String,
    onPasswordChange: (String) -> Unit,
    onTrailingIconClick: () -> Unit,
    hidePassword: Boolean
) {
    val trailingIcon =
        if (hidePassword) FontAwesomeIcon.EYESLASH
        else FontAwesomeIcon.EYE

    val visualTransformation =
        if (hidePassword) PasswordVisualTransformation()
        else VisualTransformation.None

    CustomTextField(
        modifier = modifier,
        value = password,
        onValueChange = onPasswordChange,
        leadingIcon = FontAwesomeIcon.LOCK.unicode,
        trailingIcon = trailingIcon.unicode,
        onTrailingIconClick = onTrailingIconClick,
        label = "Password",
        visualTransformation = visualTransformation,
    )
}