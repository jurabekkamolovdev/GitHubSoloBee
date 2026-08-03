package com.javohir.feature.profile.content.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.javohir.feature.R
import com.javohir.ui.theme.SoloBeeColors

@Composable
internal fun ProfileMenuRow(
    @DrawableRes iconRes: Int,
    @StringRes titleRes: Int,
    @StringRes trailingTextRes: Int?,
    onClick: () -> Unit,
    iconSize: Dp,
    titleFontSize: TextUnit,
    trailingFontSize: TextUnit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(iconSize),
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = stringResource(id = titleRes),
            style = TextStyle(
                fontSize = titleFontSize,
                fontFamily = FontFamily(Font(R.font.nunito_semibold)),
                color = SoloBeeColors.Black,
            ),
        )
        Spacer(modifier = Modifier.weight(1f))
        if (trailingTextRes != null) {
            Text(
                text = stringResource(id = trailingTextRes),
                style = TextStyle(
                    fontSize = trailingFontSize,
                    fontFamily = FontFamily(Font(R.font.nunito_reguler)),
                    color = SoloBeeColors.TextMuted,
                ),
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Icon(
            painter = painterResource(id = R.drawable.right_ic),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(iconSize),
        )
    }
}
