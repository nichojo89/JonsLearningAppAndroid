package com.nicholssoftware.jonslearningappandroid.ui.signin

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nicholssoftware.jonslearningappandroid.R

@Composable
fun SignInScreen() {
    Box {
        val signInBg = painterResource(id = R.drawable.sign_in_bg)
        Image(
            painter = signInBg,
            contentDescription = "Sign in background image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Fall in love with my beautiful app",
                color = Color.White,
                fontSize = 40.sp,
                lineHeight = 48.sp, // Set a custom line height
                textAlign = TextAlign.Center
            )
            Text(
                text = "A journey through of learning, creativity and passion.",
                color = Color.White,
                fontSize = 16.sp,
                lineHeight = 24.sp, // Set a custom line height
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            ElevatedButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {},
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = Color(0xFFAD3689),
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    "Sign in",
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Preview
@Composable
fun SignInScreenPreview() {
    SignInScreen()
}