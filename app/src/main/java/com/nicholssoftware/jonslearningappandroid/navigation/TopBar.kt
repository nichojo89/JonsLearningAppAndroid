package com.nicholssoftware.jonslearningappandroid.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.nicholssoftware.jonslearningappandroid.R
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavController,
    title: String = "",
    showBackButton: Boolean = false,
    backButtonShouldPopToSignIn: State<Boolean> = mutableStateOf(false)
) {
    val backButtonVisible = navController.previousBackStackEntry != null || showBackButton
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_topbar_bg),
            contentDescription = "Top Bar Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
        )

        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White
            ),
            title = {
                Text(
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(x = if (backButtonVisible) (-26).dp else 0.dp),
                    textAlign = TextAlign.Center,
                    text = title
                )
            },
            navigationIcon = if (backButtonVisible) {
                {
                    IconButton(onClick = {
                        if(backButtonShouldPopToSignIn.value){
                            navController.navigate(NavigationConstants.SIGN_IN){
                                popUpTo(NavigationConstants.SIGN_IN) { inclusive = true }
                            }
                        } else {
                            navController.navigateUp()

                        }
                    }) {
                        Icon(
                            modifier = Modifier.size(26.dp),
                            tint = Color.White,
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            } else {
                @Composable {}
            }
        )
    }
}

@Preview
@Composable
fun TopBarPreview(){
    val navController = rememberNavController()
    val backButtonShouldPopStack: State<Boolean> = remember {mutableStateOf(false)}
    TopBar(
        navController = navController,
        title = "Title",
        showBackButton = true,
        backButtonShouldPopToSignIn = backButtonShouldPopStack)
}