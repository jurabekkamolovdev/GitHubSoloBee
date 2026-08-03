package com.javohir.feature.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.javohir.feature.R
import com.javohir.feature.profile.content.common.DeleteAccountDialog
import com.javohir.feature.profile.content.PhoneContent
import com.javohir.feature.profile.content.TabletContent
import com.javohir.ui.component.AnimatedSnackBarHost
import kotlinx.coroutines.launch

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.profile
 * Description: ProfileScreen compose UI Screen
 */
@Composable
fun ProfileScreen(
    paddingValues: PaddingValues,
    viewModel: ProfileViewModel = hiltViewModel(),
    isTablet: Boolean,
    onBackClick: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val comingSoonMessage: String = stringResource(id = R.string.coming_soon)
    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is ProfileEvent.ShowError -> {
                    launch {
                        snackBarHostState.showSnackbar(event.message)
                    }
                }
                ProfileEvent.ShowComingSoon -> {
                    launch {
                        snackBarHostState.showSnackbar(message = comingSoonMessage)
                    }
                }
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        ProfileContent(
            paddingValues = paddingValues,
            isTablet = isTablet,
            state = state,
            onAction = viewModel::onAction,
            onBackClick = onBackClick,
        )
        AnimatedSnackBarHost(
            hostState = snackBarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = paddingValues.calculateBottomPadding() + 12.dp),
        )
        if (state.showDeleteAccountDialog) {
            DeleteAccountDialog(
                isDeletingAccount = state.isDeletingAccount,
                onConfirm = { viewModel.onAction(ProfileIntent.DeleteAccountConfirmed) },
                onDismiss = { viewModel.onAction(ProfileIntent.DeleteAccountDismissed) },
            )
        }
    }
}

@Composable
private fun ProfileContent(
    paddingValues: PaddingValues,
    isTablet: Boolean,
    state: ProfileState,
    onAction: (ProfileIntent) -> Unit,
    onBackClick: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.background_img),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
        Image(
            painter = painterResource(R.drawable.login_bg),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            contentScale = ContentScale.FillWidth,
        )
        if (isTablet) {
            TabletContent()
        } else {
            PhoneContent(
                onAction = onAction,
                onBackClick = onBackClick,
                state = state,
                paddingValues = paddingValues
            )
        }
    }
}
