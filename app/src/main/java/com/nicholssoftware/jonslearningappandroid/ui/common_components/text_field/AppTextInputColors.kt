package com.nicholssoftware.jonslearningappandroid.ui.common_components.text_field

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val AppTextInputColors: TextFieldColors
    @Composable
    get() = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.background,
        unfocusedContainerColor = Color.White,
        cursorColor = MaterialTheme.colorScheme.onBackground,
        focusedLabelColor = MaterialTheme.colorScheme.onBackground,
        unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
        focusedLeadingIconColor = MaterialTheme.colorScheme.onBackground,
        unfocusedLeadingIconColor = MaterialTheme.colorScheme.onBackground,
        focusedTrailingIconColor = MaterialTheme.colorScheme.onBackground,
        unfocusedTrailingIconColor = MaterialTheme.colorScheme.onBackground,
        errorBorderColor = MaterialTheme.colorScheme.onBackground,
        errorTextColor = MaterialTheme.colorScheme.onBackground,
        errorLeadingIconColor = MaterialTheme.colorScheme.onBackground,
        errorTrailingIconColor = MaterialTheme.colorScheme.onBackground,
        errorLabelColor = MaterialTheme.colorScheme.onBackground,
        errorSupportingTextColor = MaterialTheme.colorScheme.error,
        focusedSupportingTextColor = MaterialTheme.colorScheme.onBackground,
        unfocusedSupportingTextColor = MaterialTheme.colorScheme.onBackground
    )