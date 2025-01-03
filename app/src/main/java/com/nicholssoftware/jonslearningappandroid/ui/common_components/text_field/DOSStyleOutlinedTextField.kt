package com.nicholssoftware.jonslearningappandroid.ui.common_components.text_field

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nicholssoftware.jonslearningappandroid.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DOSStyleOutlinedTextField(
    modifier: Modifier = Modifier,
    prompt: State<String>,
    title: State<String>,
    updatePrompt: (String) -> Unit
) {
    val fontFamily = FontFamily(
        Font(R.font.press_start_2p)
    )

    OutlinedTextField(
        value = prompt.value,
        onValueChange = { updatePrompt(it) },
        label = { Text(
            title.value,
            modifier = Modifier
                .background(Color.Black)
                .border(1.dp,Color.Green)
                .padding(2.dp)
        ) },
        modifier = modifier
            .fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Green,
            unfocusedTextColor = Color.Green,
            focusedContainerColor = Color.Black,
            unfocusedContainerColor = Color.Black,
            disabledContainerColor = Color.Black,
            errorContainerColor = Color.Transparent,
            cursorColor = Color.Green,
            errorCursorColor = Color.Green,
            focusedBorderColor = Color.Green,
            unfocusedBorderColor = Color.Green,
            focusedLabelColor = Color.Green,
            unfocusedLabelColor = Color.Green
        ),
        textStyle = TextStyle(
            color = Color.Green,
            fontFamily = fontFamily,
            fontWeight = FontWeight.Thin,
            fontSize = 10.sp,
            lineHeight = 20.sp
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Default
        ),
        keyboardActions = KeyboardActions.Default
    )
}

@Preview
@Composable
fun DOSStyleOutlinedTextFieldPreview(){
    val prompt = remember { mutableStateOf("Cyberpunk warrior, futuristic, sci fi, bearded man...") }
    val title = remember { mutableStateOf("Describe your character") }
    DOSStyleOutlinedTextField(
        Modifier,
        prompt,
        title,
        { "" }
    )
}
