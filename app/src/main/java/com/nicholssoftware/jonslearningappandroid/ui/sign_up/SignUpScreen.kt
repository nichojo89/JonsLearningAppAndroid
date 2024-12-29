package com.nicholssoftware.jonslearningappandroid.ui.sign_up

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.nicholssoftware.jonslearningappandroid.R
import com.nicholssoftware.jonslearningappandroid.ui.navigation.TopBar
import com.nicholssoftware.jonslearningappandroid.ui.common_components.text_field.EmailTextField
import com.nicholssoftware.jonslearningappandroid.ui.common_components.text_field.PasswordTextField
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun SignUpScreen(
    signUp: () -> Unit = {},
    navController: NavController,
    usernameFlow: State<String>,
    passwordFlow: State<String>,
    signInEnabled: State<Boolean>,
    resetNavigation: () -> Unit = {},
    passwordErrorMessage: State<String>,
    usernameErrorMessage: State<String>,
    navigationEvent: StateFlow<String?>,
    confirmPasswordFlow: State<String>,
    validateCredentials: () -> Unit = {},
    updateUsername: (String) -> Unit = {},
    updatePassword: (String) -> Unit = {},
    requestSignInWithGoogle: () -> Unit = {},
    isConfirmPasswordVisible: State<Boolean>,
    updateConfirmPasswordFlow: (String) -> Unit,
    signIntoGoogle: (newImplementation: (GoogleSignInAccount) -> Unit) -> Unit,
    signUpWithGoogle: (account: GoogleSignInAccount, navController: NavController) -> Unit = { _: GoogleSignInAccount, _: NavController -> }
) {
    val googleSignIn: (GoogleSignInAccount) -> Unit = {
        signUpWithGoogle(it, navController)
    }

    signIntoGoogle(googleSignIn)

    val onUsernameUpdate: (String) -> Unit = {
        updateUsername.invoke(it)
        validateCredentials()
    }
    val onPasswordUpdate: (String) -> Unit = {
        updatePassword.invoke(it)
        validateCredentials()
    }
    val onConfirmPasswordUpdate: (String) -> Unit = {
        updateConfirmPasswordFlow.invoke(it)
        validateCredentials()
    }
    val hidePassword = remember {
        mutableStateOf(true)
    }

    val navEvent = navigationEvent.collectAsState()
    LaunchedEffect(key1 = navEvent.value) {
        navigationEvent.value?.let { destination ->
            navController.navigate(destination)
            resetNavigation.invoke()
        }
    }
    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                title = "New Account",
                showBackButton = true)
        },
        content = { paddingValues ->
            BoxWithConstraints {
                val height = maxHeight
                Image(
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = R.drawable.sign_in_bg),
                    contentDescription = stringResource(id = R.string.sign_up_background_image)
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
                        Text(
                            stringResource(id = R.string.get_started),
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
                            stringResource(id = R.string.create_free_account),
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            style = TextStyle(
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        )
                        Spacer(modifier = Modifier.height((height.value * 0.03).dp))

                        EmailTextField(
                            signIn = signUp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            email = usernameFlow.value,
                            onEmailChange = onUsernameUpdate,
                            errorMessage = usernameErrorMessage.value,
                            label = stringResource(id = R.string.username),
                        )

                        PasswordTextField(
                            signIn = signUp,
                            modifier = Modifier
                                .fillMaxWidth(),
                            password = passwordFlow.value,
                            hidePassword = hidePassword.value,
                            onPasswordChange = onPasswordUpdate,
                            label = stringResource(id = R.string.password),
                            onTrailingIconClick = { hidePassword.value = !hidePassword.value }
                        )
                        if (isConfirmPasswordVisible.value) {
                            PasswordTextField(
                                label = stringResource(id = R.string.confirm_password),
                                signIn = signUp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp),
                                password = confirmPasswordFlow.value,
                                hidePassword = hidePassword.value,
                                onPasswordChange = onConfirmPasswordUpdate,
                                errorMessage = passwordErrorMessage.value,
                                onTrailingIconClick = { hidePassword.value = !hidePassword.value }
                            )
                        }

                        Spacer(modifier = Modifier.height((height.value * 0.03).dp))
                        Box(modifier = Modifier.height(20.dp)) {
                            Box(
                                modifier = Modifier
                                    .zIndex(1f)
                                    .padding(top = 3.dp)
                                    .align(Alignment.Center)
                                    .background(Color.Gray)
                                    .height(1.dp)
                                    .fillMaxWidth()
                            )
                            Text(
                                stringResource(id = R.string.or_sign_up_with),
                                fontSize = 16.sp,
                                color = Color.Gray,
                                modifier = Modifier
                                    .zIndex(2f)
                                    .align(Alignment.Center)
                                    .background(Color.White)
                                    .padding(horizontal = 12.dp)
                            )
                        }
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                            .border(
                                BorderStroke(1.dp, Color.Gray),
                                RoundedCornerShape(14.dp)
                            )
                            .clickable {
                                requestSignInWithGoogle.invoke()
                            }
                        ) {
                            Image(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(48.dp)
                                    .padding(12.dp),
                                painter = painterResource(id = R.drawable.ic_google),
                                contentDescription = stringResource(id = R.string.sign_up_with_google),
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Row(
                            modifier = Modifier
                                .padding(vertical = 16.dp)
                                .align(Alignment.CenterHorizontally)
                        ) {
                            Text(
                                stringResource(id = R.string.already_have_an_account),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                stringResource(id = R.string.sign_in),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = Color(0xFFAD3689),
                                modifier = Modifier
                                    .padding(start = 6.dp)
                                    .clickable {
                                        navController.popBackStack()
                                    })
                        }
                        Button(
                            enabled = signInEnabled.value,
                            onClick = {
                                signUp()
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
                            Text(stringResource(id = R.string.sign_up))
                        }
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun SignInScreenPreview() {
    val navController = rememberNavController()
    val navigationEvent = remember { MutableStateFlow("") }
    val signInEnabledState = remember { mutableStateOf(true) }
    val usernameState = remember { mutableStateOf("TestUser") }
    val passwordState = remember { mutableStateOf("Password123") }
    val usernameErrorMessageState = remember { mutableStateOf("") }
    val passwordErrorMessageState = remember { mutableStateOf("") }
    val isConfirmPasswordVisible = remember { mutableStateOf(true) }
    val confirmPasswordState = remember { mutableStateOf("Password123") }

    SignUpScreen(
        signUp = {},
        signIntoGoogle = {},
        resetNavigation = {},
        validateCredentials = {},
        usernameFlow = usernameState,
        passwordFlow = passwordState,
        navController = navController,
        updateConfirmPasswordFlow = {},
        navigationEvent = navigationEvent,
        signInEnabled = signInEnabledState,
        confirmPasswordFlow = confirmPasswordState,
        updateUsername = { usernameState.value = it },
        updatePassword = { passwordState.value = it },
        usernameErrorMessage = usernameErrorMessageState,
        passwordErrorMessage = passwordErrorMessageState,
        isConfirmPasswordVisible = isConfirmPasswordVisible,
        signUpWithGoogle = { googleSignInAccount: GoogleSignInAccount, navController: NavController -> }
    )
}