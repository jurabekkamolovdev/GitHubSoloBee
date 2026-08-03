package com.javohir.feature.login.content

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.login.content
 * Description: login/PhoneContent: qurilma o'lchami bo'yicha Compose kontent.
 */

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.javohir.feature.R
import com.javohir.feature.login.LoginIntent
import com.javohir.feature.login.LoginState
import com.javohir.ui.component.GlossyPrimaryButton
import com.javohir.ui.component.LoginTextField
import com.javohir.ui.theme.SoloBeeColors

@Composable
fun PhoneContent(
    topPadding: Dp,
    state: LoginState,
    onAction: (LoginIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = topPadding)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.login_ic),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = stringResource(id = R.string.username),
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.nunito_semibold)),
                color = SoloBeeColors.Black
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        LoginTextField(
            value = state.usernameText,
            onValueChange = { onAction(LoginIntent.UserNameChanged(it)) },
            hintText = stringResource(id = R.string.username_hint),
            hintFont = FontFamily(Font(resId = R.font.nunito_reguler)),
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Text(
            text = stringResource(id = R.string.password),
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.nunito_semibold)),
                color = SoloBeeColors.Black
            ),
            modifier = Modifier
                .padding(top = 20.dp)
                .padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        LoginTextField(
            value = state.passwordText,
            onValueChange = { onAction(LoginIntent.PasswordChanged(it)) },
            hintText = stringResource(id = R.string.password_hint),
            hintFont = FontFamily(Font(resId = R.font.nunito_reguler)),
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        GlossyPrimaryButton(
            text = stringResource(id = R.string.login),
            fontFamily = FontFamily(Font(resId = R.font.baloo2_bold)),
            isLoading = state.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 26.dp),
            onClick = { onAction(LoginIntent.LoginClicked) }
        )
    }
}
