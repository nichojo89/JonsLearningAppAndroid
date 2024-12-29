package com.nicholssoftware.jonslearningappandroid.ui.common_components.text_field

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nicholssoftware.jonslearningappandroid.ui.common_components.font.FontAwesomeIcon
import com.nicholssoftware.jonslearningappandroid.ui.common_components.font.fontAwesomeFontFamily

private val AppTextInputIconSize = 18.dp

@Composable
fun CustomTextField(
    value: String,
    onDone: () -> Unit,
    label: String? = null,
    errorMessage: String = "",
    leadingIcon: String? = null,
    trailingIcon: String? = null,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    onTrailingIconClick: () -> Unit = {},
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Column {
        OutlinedTextField(
            value = value,
            modifier = modifier,
            colors = AppTextInputColors,
            onValueChange = onValueChange,
            shape = RoundedCornerShape(50.dp),
            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    onDone.invoke()
                }
            ),
            label = label?.let{
                @Composable {
                    Text(text = it)
                }
            },
            leadingIcon = leadingIcon?.let {
                @Composable {
                    Text(leadingIcon,
                        fontFamily = fontAwesomeFontFamily,
                        modifier = Modifier.size(AppTextInputIconSize))
                }
            },
            trailingIcon = trailingIcon?.let {
                @Composable {
                    IconButton(onClick = onTrailingIconClick) {
                        Text(trailingIcon,
                            fontFamily = fontAwesomeFontFamily,
                            modifier = Modifier.size(AppTextInputIconSize))
                    }
                }
            },
        )
        Text(errorMessage,
            color = Color.Red,
            fontSize = 12.sp)
    }
}
@Preview(showBackground = true)
@Composable
fun CustomTextFieldPreview() {
    CustomTextField(
        modifier = Modifier.padding(16.dp),
        value = FontAwesomeIcon.ENVELOPE.unicode,
        onValueChange = {},
        leadingIcon = "search",
        onTrailingIconClick = { },
        trailingIcon = "close",
        label = "Label",
        errorMessage = "Error message here",
        onDone = {}
    )
}