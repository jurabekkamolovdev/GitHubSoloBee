package com.javohir.feature.welcome.content

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.welcome.content
 * Description: welcome/PhoneContent: qurilma o'lchami bo'yicha Compose kontent.
 */

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.javohir.feature.R
import com.javohir.feature.welcome.WelcomeIntent
import com.javohir.feature.welcome.WelcomeState
import com.javohir.ui.component.GlossyPrimaryButton

@Composable
fun PhoneContent(
    topPadding: Dp,
    state: WelcomeState,
    onAction: (WelcomeIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = topPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.login_ic),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .fillMaxWidth(fraction = 0.45f)
                .height(160.dp)
        )

        Spacer(modifier = Modifier.height(72.dp))

        GlossyPrimaryButton(
            text = stringResource(id = R.string.login),
            fontFamily = FontFamily(Font(resId = R.font.baloo2_bold)),
            isLoading = state.isLoading,
            modifier = Modifier.fillMaxWidth(),
            onClick = { onAction(WelcomeIntent.LoginClicked) }
        )

        GlossyPrimaryButton(
            text = stringResource(id = R.string.sign_up),
            fontFamily = FontFamily(Font(resId = R.font.baloo2_bold)),
            isLoading = state.isLoading,
            modifier = Modifier.fillMaxWidth(),
            onClick = { onAction(WelcomeIntent.SignUpClicked) }
        )
    }
}