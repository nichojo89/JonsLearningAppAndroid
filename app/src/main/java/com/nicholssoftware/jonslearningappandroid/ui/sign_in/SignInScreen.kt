package com.nicholssoftware.jonslearningappandroid.ui.sign_in

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.nicholssoftware.jonslearningappandroid.R
import com.nicholssoftware.jonslearningappandroid.domain.network.NavigationConstants
import com.nicholssoftware.jonslearningappandroid.ui.common_components.text_field.EmailTextField
import com.nicholssoftware.jonslearningappandroid.ui.common_components.text_field.PasswordTextField
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun SignInScreen(
    signIn: () -> Unit = {},
    usernameFlow: State<String>,
    passwordFlow: State<String>,
    navController: NavController,
    rememberUser: State<Boolean>,
    signInEnabled: State<Boolean>,
    createAccount: () -> Unit = {},
    resetNavigation: () -> Unit = {},
    userCanSignIn: StateFlow<Boolean?>,
    navigationEvent: StateFlow<String?>,
    sendForgotPassword: () -> Unit = {},
    usernameErrorMessage: State<String>,
    passwordErrorMessage: State<String>,
    validateCredentials: () -> Unit = {},
    updateUsername: (String) -> Unit = {},
    updatePassword: (String) -> Unit = {},
    requestSignInWithGoogle: () -> Unit = {},
    updateRememberUser: (Boolean) -> Unit = {},
    updateNavController: (NavController) -> Unit = {},
    signIntoGoogle: (newImplementation: (GoogleSignInAccount) -> Unit) -> Unit,
    signInWithGoogle: (account: GoogleSignInAccount, navController: NavController) -> Unit = { _: GoogleSignInAccount, _: NavController -> }
) {
    updateNavController(navController)
    val googleSignIn: (GoogleSignInAccount) -> Unit = {
        signInWithGoogle(it, navController)
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
    val userSignedIn = userCanSignIn.collectAsState()
    LaunchedEffect(userSignedIn.value) {
       if(userSignedIn.value == true) {
//           navController.navigate(NavigationConstants.DASHBOARD){
//               popUpTo(NavigationConstants.DASHBOARD) { inclusive = true }
//           }
           navController.navigate(NavigationConstants.CHARACTER_GENERATION){
               popUpTo(NavigationConstants.CHARACTER_GENERATION) { inclusive = true }
           }
       }
    }

    BoxWithConstraints {
        val height = maxHeight
        Image(
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.sign_in_bg),
            contentDescription = stringResource(id = R.string.sign_in_background_image)
        )

        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp)
                .clip(RoundedCornerShape(16.dp)),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            if(userSignedIn.value == null){
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                ) {
                    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.progress_indicator))
                    val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)
                    LottieAnimation(
                        progress = progress,
                        composition = composition,
                        modifier = Modifier.size(200.dp)
                    )
                }
            } else if(userSignedIn.value == false) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxSize()
                ) {
                    Text(
                        stringResource(id = R.string.welcome_back),
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
                        stringResource(id = R.string.enter_details_below),
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        style = TextStyle(
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                    )
                    Spacer(modifier = Modifier.height((height.value * 0.03).dp))
                    EmailTextField(
                        signIn = signIn,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        email = usernameFlow.value,
                        onEmailChange = onUsernameUpdate,
                        errorMessage = usernameErrorMessage.value,
                        label = stringResource(id = R.string.username)
                    )

                    PasswordTextField(
                        signIn = signIn,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        password = passwordFlow.value,
                        hidePassword = hidePassword.value,
                        onPasswordChange = onPasswordUpdate,
                        label = stringResource(id = R.string.password),
                        errorMessage = passwordErrorMessage.value,
                        onTrailingIconClick = { hidePassword.value = !hidePassword.value }
                    )

                    Row(modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFAD3689),
                            text = stringResource(id = R.string.forgot_password),
                            modifier = Modifier
                                .clickable {
                                    sendForgotPassword.invoke()
                                }
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            fontSize = 16.sp,
                            text = "Remember me",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFAD3689),
                            modifier = Modifier
                                .clickable {
                                    updateRememberUser.invoke(!rememberUser.value)
                                }
                        )
                        Checkbox(
                            checked = rememberUser.value,
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xFFAD3689),
                                uncheckedColor = Color.Gray,
                                checkmarkColor = Color.White
                            ),
                            onCheckedChange = {updateRememberUser.invoke(!rememberUser.value)}
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
                            stringResource(id = R.string.or_sign_in_with),
                            fontSize = 16.sp,
                            color = Color.Gray,
                            modifier = Modifier
                                .zIndex(2f)
                                .align(Alignment.Center)
                                .background(Color.White)
                                .padding(horizontal = 12.dp),
                        )
                    }
                    Box(modifier = Modifier
                        .padding(top = 16.dp)
                        .border(
                            BorderStroke(1.dp, Color.Gray),
                            RoundedCornerShape(14.dp)
                        )
                        .fillMaxWidth()
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
                            contentDescription = stringResource(id = R.string.sign_in_with_google)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            stringResource(id = R.string.need_an_account),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            stringResource(id = R.string.need_an_account),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = Color(0xFFAD3689),
                            modifier = Modifier
                                .padding(start = 6.dp)
                                .clickable {
                                    createAccount.invoke()
                                })
                    }
                    Button(
                        enabled = signInEnabled.value,
                        onClick = {
                            signIn()
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
                        Text(stringResource(id = R.string.sign_in))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignInScreenPreview() {
    val navController = rememberNavController()
    val rememberUser = remember { mutableStateOf(false) }
    val signInEnabledState = remember { mutableStateOf(true) }
    val usernameState = remember { mutableStateOf("TestUser") }
    val passwordState = remember { mutableStateOf("Password123") }
    val usernameErrorMessageState = remember { mutableStateOf("") }
    val passwordErrorMessageState = remember { mutableStateOf("") }
    val navigationEvent = remember {
        MutableStateFlow("")
    }
    val signInRequired = remember {MutableStateFlow(false)}

    SignInScreen(
        createAccount = {},
        signIntoGoogle = {},
        resetNavigation = {},
        sendForgotPassword = {},
        validateCredentials = {},
        rememberUser = rememberUser,
        usernameFlow = usernameState,
        passwordFlow = passwordState,
        navController = navController,
        userCanSignIn = signInRequired,
        navigationEvent = navigationEvent,
        signInEnabled = signInEnabledState,
        updateUsername = { usernameState.value = it },
        updatePassword = { passwordState.value = it },
        usernameErrorMessage = usernameErrorMessageState,
        passwordErrorMessage = passwordErrorMessageState,
        signInWithGoogle = { googleSignInAccount: GoogleSignInAccount, navController: NavController -> }
    )
}