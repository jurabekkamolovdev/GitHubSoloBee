package com.javohir.feature.welcome.content

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.welcome.content
 * Description: welcome/TabletContent: qurilma o'lchami bo'yicha Compose kontent.
 */

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import com.javohir.feature.welcome.WelcomeIntent
import com.javohir.feature.welcome.WelcomeState

@Composable
fun TabletContent(
    topPadding: Dp,
    state: WelcomeState,
    onAction: (WelcomeIntent) -> Unit
) {
    PhoneContent(
        topPadding = topPadding,
        state = state,
        onAction = onAction
    )
}