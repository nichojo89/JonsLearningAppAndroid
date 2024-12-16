package com.nicholssoftware.jonslearningappandroid.ui.login.email_verification

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.nicholssoftware.jonslearningappandroid.R
import com.nicholssoftware.jonslearningappandroid.navigation.TopBar

@Composable
fun EmailVerificationScreen(
    navController: NavController,
    isSendEmailEnabled: State<Boolean>,
    sendVerificationEmail: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                title = "Verification",
                showBackButton = true)
        },
        content = { paddingValues ->
            BoxWithConstraints {
                val height = maxHeight
                Image(
                    contentScale = ContentScale.Crop,
                    painter = painterResource(id = R.drawable.sign_in_bg),
                    contentDescription = stringResource(id = R.string.sign_in_background_image),
                    modifier = Modifier.fillMaxSize()
                )

                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = paddingValues.calculateTopPadding() + 32.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxSize()
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            "Email verification required",
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = (height.value * 0.1).dp),
                            style = TextStyle(
                                fontSize = 32.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            "Send email verification",
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            style = TextStyle(
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        )

                        Spacer(modifier = Modifier.padding((height.value * 0.03).dp))

                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center // Ensure center alignment inside Box
                        ) {
                            Image(
                                alignment = Alignment.Center,
                                contentScale = ContentScale.Crop,
                                painter = painterResource(id = R.drawable.ic_email_verification),
                                contentDescription = "Email verification image"
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))

                        Button(
                            enabled = isSendEmailEnabled.value,
                            onClick = {
                                sendVerificationEmail()
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
                        ) {
                            Text("Send")
                        }
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun EmailVerificationScreenPreview() {
    val isSignInEnabled = remember { mutableStateOf(false) }
    val sendVerificationEmail: () -> Unit = {}
    val navController = rememberNavController()
    EmailVerificationScreen(
        navController = navController,
        isSendEmailEnabled = isSignInEnabled,
        sendVerificationEmail = sendVerificationEmail
    )
}