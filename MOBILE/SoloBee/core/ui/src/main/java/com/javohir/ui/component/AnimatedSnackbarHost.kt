package com.javohir.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.ui.component
 * Description: Snack bar Animate
 */
@Composable
fun AnimatedSnackBarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier
){
    var shownData by remember { mutableStateOf<SnackbarData?>(null) }
    var isVisible by remember { mutableStateOf(false) }
    val currentData = hostState.currentSnackbarData

    SnackbarHost(
        hostState = hostState,
        modifier = Modifier.size(0.dp)
    )

    LaunchedEffect(currentData) {
        when {
            currentData == null -> {
                isVisible = false
                delay(260L)
                shownData = null
            }

            shownData == null -> {
                shownData = currentData
                isVisible = true
            }

            shownData !== currentData -> {
                isVisible = false
                delay(220L)
                shownData = currentData
                isVisible = true
            }
        }
    }

    Box(modifier = modifier) {
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(
                initialOffsetY = { -it * 2 },
                animationSpec = tween(durationMillis = 420)
            ) + fadeIn(animationSpec = tween(durationMillis = 320)) + scaleIn(
                initialScale = 0.84f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
            exit = slideOutVertically(
                targetOffsetY = { -it / 2 },
                animationSpec = tween(durationMillis = 260)
            ) + fadeOut(animationSpec = tween(durationMillis = 220)) + scaleOut(
                targetScale = 0.92f,
                animationSpec = tween(durationMillis = 220)
            )
        ) {
            shownData?.let { data ->
                Snackbar(snackbarData = data)
            }
        }
    }
}


