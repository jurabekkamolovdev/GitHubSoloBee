package com.javohir.feature.register.content.step

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.register.content.step
 * Description: 2-bosqich — username va parol.
 */

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.javohir.feature.R
import com.javohir.feature.register.RegisterIntent
import com.javohir.feature.register.RegisterState
import com.javohir.feature.register.content.common.RegisterFormField
import com.javohir.feature.register.content.common.RegisterLogo
import com.javohir.ui.component.GlossyPrimaryButton

@Composable
fun AccountInfoStep(
    topPadding: Dp,
    state: RegisterState,
    onAction: (RegisterIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .padding(top = topPadding)
    ) {
        RegisterLogo()

        Spacer(modifier = Modifier.height(20.dp))

        RegisterFormField(
            label = stringResource(id = R.string.username),
            value = state.userName,
            onValueChange = { value -> onAction(RegisterIntent.UserNameChanged(value)) },
            hintText = stringResource(id = R.string.username_hint)
        )

        RegisterFormField(
            label = stringResource(id = R.string.password),
            value = state.password,
            onValueChange = { value -> onAction(RegisterIntent.PasswordChanged(value)) },
            hintText = stringResource(id = R.string.password_hint),
            modifier = Modifier.padding(top = 16.dp),
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            isPassword = true,
            isPasswordVisible = state.isPasswordVisible,
            onTogglePasswordVisibility = { onAction(RegisterIntent.TogglePasswordVisibility) }
        )

        GlossyPrimaryButton(
            text = stringResource(id = R.string.register_continue),
            fontFamily = FontFamily(Font(resId = R.font.baloo2_bold)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            onClick = { onAction(RegisterIntent.ContinueClicked) }
        )
    }
}