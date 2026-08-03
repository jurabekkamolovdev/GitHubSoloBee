package com.javohir.solobee.navigation

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.solobee.navigation
 * Description: NavTransitions: navigatsiya o'tish animatsiyalari.
 */

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavBackStackEntry

object NavTransitions {
    val enter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        slideInHorizontally(
            initialOffsetX = { it },
            animationSpec = tween(durationMillis = 700)
        ) +
                fadeIn(animationSpec = tween(durationMillis = 700)) +
                scaleIn(
                    initialScale = 0.9f,
                    animationSpec = tween(durationMillis = 700)
                )
    }

    val exit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        slideOutHorizontally(
            targetOffsetX = { -it },
            animationSpec = tween(durationMillis = 600)
        ) +
                fadeOut(animationSpec = tween(durationMillis = 600)) +
                scaleOut(
                    targetScale = 1.05f,
                    animationSpec = tween(durationMillis = 600)
                )
    }

    val popEnter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        slideInHorizontally(
            initialOffsetX = { -it },
            animationSpec = tween(durationMillis = 700)
        ) +
                fadeIn(animationSpec = tween(durationMillis = 700)) +
                scaleIn(
                    initialScale = 0.9f,
                    animationSpec = tween(durationMillis = 700)
                )
    }

    val popExit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        slideOutHorizontally(
            targetOffsetX = { it },
            animationSpec = tween(durationMillis = 600)
        ) +
                fadeOut(animationSpec = tween(durationMillis = 600)) +
                scaleOut(
                    targetScale = 1.05f,
                    animationSpec = tween(durationMillis = 600)
                )
    }
}
