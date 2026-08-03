package com.javohir.feature.register.content

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.register.content
 * Description: register/PhoneContent: qurilma o'lchami bo'yicha Compose kontent.
 */

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import com.javohir.feature.register.RegisterIntent
import com.javohir.feature.register.RegisterState
import com.javohir.feature.register.RegisterStep
import com.javohir.feature.register.content.step.AccountInfoStep
import com.javohir.feature.register.content.step.PersonalInfoStep
import com.javohir.feature.register.content.step.SelectAvatarStep
import com.javohir.feature.register.content.step.SuccessStep

@Composable
fun PhoneContent(
    topPadding: Dp,
    state: RegisterState,
    onAction: (RegisterIntent) -> Unit
) {
    when (state.step) {
        RegisterStep.PERSONAL_INFO -> PersonalInfoStep(
            topPadding = topPadding,
            state = state,
            onAction = onAction
        )

        RegisterStep.ACCOUNT_INFO -> AccountInfoStep(
            topPadding = topPadding,
            state = state,
            onAction = onAction
        )

        RegisterStep.SELECT_AVATAR -> SelectAvatarStep(
            topPadding = topPadding,
            state = state,
            onAction = onAction
        )

        RegisterStep.SUCCESS -> SuccessStep(
            topPadding = topPadding,
            onFinished = { onAction(RegisterIntent.SuccessShown) }
        )
    }
}