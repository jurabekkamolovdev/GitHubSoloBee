package com.javohir.feature.welcome
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.javohir.feature.R
import com.javohir.feature.welcome.content.PhoneContent
import com.javohir.feature.welcome.content.TabletContent
import com.javohir.ui.theme.SoloBeeColors

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.welcome
 * Description: Welcome Screen
 */
@Composable
fun WelcomeScreen(
    paddingValues: PaddingValues,
    isTablet: Boolean,
    viewModel: WelcomeViewModel = hiltViewModel(),
    navigateToLogin: () -> Unit,
    navigateToRegister: () -> Unit
){
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when(event){
                is WelcomeEvent.NavigateToLogin -> navigateToLogin()

                is WelcomeEvent.NavigateToRegister -> navigateToRegister()
            }
        }
    }
    WelcomeContent(
        paddingValues = paddingValues,
        isTablet = isTablet,
        state = state,
        onAction = viewModel::onAction
    )
}
@Composable
fun WelcomeContent(
    paddingValues: PaddingValues,
    isTablet: Boolean,
    state: WelcomeState,
    onAction: (WelcomeIntent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SoloBeeColors.White)
    ) {
        Image(
            painter = painterResource(id = R.drawable.login_bg),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            contentScale = ContentScale.FillWidth
        )

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val topPadding = maxHeight * 0.08f
            if (isTablet) {
                TabletContent(
                    topPadding = topPadding,
                    state = state,
                    onAction = onAction
                )
            } else {
                PhoneContent(
                    topPadding = topPadding,
                    state = state,
                    onAction = onAction
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun WelcomePreview(){
    WelcomeContent(
        paddingValues = PaddingValues(all = 12.dp),
        isTablet = false,
        state = WelcomeState(),
        onAction = {}
    )
}