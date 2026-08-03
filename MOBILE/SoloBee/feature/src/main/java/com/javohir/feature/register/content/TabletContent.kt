package com.javohir.feature.register.content

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.register.content
 * Description: register/TabletContent: qurilma o'lchami bo'yicha Compose kontent.
 */

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import com.javohir.feature.register.RegisterIntent
import com.javohir.feature.register.RegisterState

@Composable
fun TabletContent(
    topPadding: Dp,
    state: RegisterState,
    onAction: (RegisterIntent) -> Unit
) {
    PhoneContent(
        topPadding = topPadding,
        state = state,
        onAction = onAction
    )
}