package com.javohir.feature.splash
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.javohir.feature.R
import com.javohir.ui.theme.SoloBeeColors
/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.splash
 * Description: Splash Screen
 */

@Composable
fun SplashScreen(
    paddingValues: PaddingValues,
    navigateToOnBoarding: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToWelcome: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
){
    LaunchedEffect(Unit) {
       viewModel.event.collect { event ->
           when(event){
               SplashEvent.NavigateToHome -> navigateToHome()
               SplashEvent.NavigateToWelcome -> navigateToWelcome()
               SplashEvent.NavigateToOnBoarding -> navigateToOnBoarding()
           }
       }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues = paddingValues)
        .background(color = SoloBeeColors.White),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_img),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(0.65f)
                .fillMaxHeight(0.65f),
            contentScale = ContentScale.Fit
        )
    }
}
@Composable
@Preview()
fun SplashPreview(){
    SplashScreen(
        navigateToOnBoarding = {},
        paddingValues = PaddingValues(all = 12.dp),
        navigateToHome = {},
        navigateToWelcome = {}
    )
}