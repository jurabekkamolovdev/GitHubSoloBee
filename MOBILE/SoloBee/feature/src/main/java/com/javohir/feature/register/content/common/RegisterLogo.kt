package com.javohir.feature.register.content.common

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.register.content.common
 * Description: Barcha bosqichlar tepasida takrorlanadigan logotip.
 */

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.javohir.feature.R

@Composable
internal fun RegisterLogo(
    modifier: Modifier = Modifier,
    size: Dp = 120.dp
) {
    Icon(
        painter = painterResource(id = R.drawable.login_ic),
        contentDescription = null,
        tint = Color.Unspecified,
        modifier = modifier
            .fillMaxWidth()
            .height(size)
    )
}