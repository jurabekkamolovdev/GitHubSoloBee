package com.javohir.ui.component

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.ui.component
 * Description: ShimmerSkeleton: qayta foydalaniladigan UI komponent.
 */

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

fun Modifier.shimmerSkeleton(
    visible: Boolean,
    shape: Shape,
): Modifier = composed {
    if (!visible) {
        return@composed this
    }

    val transition = rememberInfiniteTransition(label = "shimmer_transition")
    val progress = transition.animateFloat(
        initialValue = -1f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_progress",
    )
    this
        .clip(shape)
        .drawWithContent {
            val width = size.width
            val height = size.height
            val startX = width * progress.value
            val endX = startX + (width * 0.45f)
            val brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFFE7EAF0),
                    Color(0xFFF4F6FA),
                    Color(0xFFE7EAF0),
                ),
                start = Offset(startX, 0f),
                end = Offset(endX, height),
            )
            drawRect(brush = brush)
        }
}
