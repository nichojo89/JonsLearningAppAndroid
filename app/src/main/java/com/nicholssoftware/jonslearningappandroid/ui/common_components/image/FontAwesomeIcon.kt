package com.nicholssoftware.jonslearningappandroid.ui.common_components.image

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.nicholssoftware.jonslearningappandroid.ui.common_components.font.fontAwesomeFontFamily

@Composable
fun FontAwesomeIcon(icon: String = "\uf004") {
    Text(
        text = icon,
        style = TextStyle(
            fontFamily = fontAwesomeFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp)
    )
}