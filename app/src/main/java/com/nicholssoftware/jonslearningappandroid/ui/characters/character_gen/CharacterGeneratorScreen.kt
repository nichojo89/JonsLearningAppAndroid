package com.nicholssoftware.jonslearningappandroid.ui.characters.character_gen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nicholssoftware.jonslearningappandroid.ui.common_components.text_field.DOSStyleOutlinedTextField

@Composable
fun CharacterGeneratorScreen(
    prompt: State<String>,
    title: State<String>,
    generateCharacterEnabled: State<Boolean>,
    updatePrompt: (String) -> Unit,
    generateCharacter: () -> Unit,
    ) {
    Scaffold(
        content = { paddingValues ->
            BoxWithConstraints(modifier = Modifier.background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF8A2BE2), // BlueViolet
                        Color(0xFF9370DB), // MediumPurple
                        Color(0xFF8A2BE2)  // Orchid
                    )
                )
            )) {
                val width = maxWidth
                val height = maxHeight
                Column( modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding())
                    .padding(horizontal = 12.dp)) {
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        modifier = Modifier

                            .padding(horizontal = width.div(6))
                        ,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        GradientBorderRectangle(width)
                        Text("Select an image to base your character from",
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(top = 12.dp))

                    }
                    Spacer(modifier = Modifier.weight(1f))
                    DOSStyleOutlinedTextField(
                        Modifier.height(height.div(4)),
                        prompt,
                        title,
                        updatePrompt
                    )

                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        enabled = generateCharacterEnabled.value,
                        onClick = {
                            generateCharacter.invoke()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 50.dp),

                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFAD3689),
                            contentColor = Color.White,
                            disabledContainerColor = Color(0x80AD3689),
                            disabledContentColor = Color.White
                        ),
                    )
                    {
                        Text("Generate")
                    }
                }
            }
        }
    )
}

@Composable
fun GradientBorderRectangle(maxWidth: Dp) {
    val width = maxWidth.value.div(1.5)
    val height = width.times(1.33)
    val plusSize = width.div(6)

    Box(
        modifier = Modifier
            .width(width.dp)
            .height(height.dp)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(16.dp),
                clip = false
            )
    ) {
        Canvas(
            modifier = Modifier.matchParentSize()
        ) {
            val gradientBrush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF8A2BE2),
                    Color(0xFF9370DB),
                    Color(0xFFBA55D3)
                )
            )
            drawRect(
                color = Color(0xFF4B0082),
                size = size
            )
            drawRect(
                brush = gradientBrush,
                size = size,
                style = Stroke(width = 12.dp.toPx())
            )
        }

        Text(
            text = "+",
            color = Color.White,
            fontSize = (plusSize).sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Preview
@Composable
fun CharacterGeneratorScreenPreview(){
    val prompt = remember { mutableStateOf("Cyberpunk warrior, futuristic, sci fi, bearded man...") }
    val title = remember { mutableStateOf("asd") }
    val generateCharacterEnabled = remember { mutableStateOf(false) }
    CharacterGeneratorScreen(
        prompt,
        title,
        generateCharacterEnabled,
        {"asdd"},
        {}
    )
}