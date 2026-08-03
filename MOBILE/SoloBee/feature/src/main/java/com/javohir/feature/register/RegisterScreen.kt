package com.javohir.feature.register
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.javohir.feature.R
import com.javohir.feature.register.content.PhoneContent
import com.javohir.feature.register.content.TabletContent
import com.javohir.ui.component.AnimatedSnackBarHost
import com.javohir.ui.theme.SoloBeeColors
import kotlinx.coroutines.launch

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.register
 * Description: Register Screen — 4 bosqich bitta route ustida.
 */
@Composable
fun RegisterScreen(
    paddingValues: PaddingValues,
    isTablet: Boolean,
    viewModel: RegisterViewModel = hiltViewModel(),
    navigateToLogin: () -> Unit,
    navigateBack: () -> Unit
){
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    // SUCCESS bosqichida orqaga qaytish bloklanadi — 2 soniya kutiladi
    BackHandler(enabled = state.step != RegisterStep.SUCCESS) {
        viewModel.onAction(RegisterIntent.BackClicked)
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when(event){
                is RegisterEvent.NavigateToLogin -> navigateToLogin()

                is RegisterEvent.NavigateBack -> navigateBack()

                is RegisterEvent.ShowErrorText -> {
                    launch {
                        snackbarHostState.showSnackbar(event.message)
                    }
                }

                is RegisterEvent.ShowErrorRes -> {
                    launch {
                        snackbarHostState.showSnackbar(context.getString(event.messageId))
                    }
                }
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        RegisterContent(
            paddingValues = paddingValues,
            isTablet = isTablet,
            state = state,
            onAction = viewModel::onAction
        )
        AnimatedSnackBarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(
                    top = paddingValues.calculateTopPadding() + 12.dp,
                    start = 16.dp,
                    end = 16.dp
                )
        )
    }
}
@Composable
fun RegisterContent(
    paddingValues: PaddingValues,
    isTablet: Boolean,
    state: RegisterState,
    onAction: (RegisterIntent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SoloBeeColors.White)
    ) {
        // Muvaffaqiyat ekrani dizaynda toza oq fonda — o't/bulut rasmisiz
        if (state.step != RegisterStep.SUCCESS) {
            Image(
                painter = painterResource(id = R.drawable.login_bg),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                contentScale = ContentScale.FillWidth
            )
        }

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val topPadding = maxHeight * 0.03f
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
fun RegisterPreview(){
    RegisterContent(
        paddingValues = PaddingValues(all = 12.dp),
        isTablet = false,
        state = RegisterState(),
        onAction = {}
    )
}