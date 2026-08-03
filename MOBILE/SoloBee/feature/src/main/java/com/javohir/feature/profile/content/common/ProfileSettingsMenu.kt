package com.javohir.feature.profile.content.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.javohir.feature.profile.ProfileIntent

@Composable
internal fun ProfileSettingsMenu(
    onAction: (ProfileIntent) -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Dp = 24.dp,
    titleFontSize: TextUnit = 20.sp,
    trailingFontSize: TextUnit = 16.sp,
) {
    val menuItems: List<ProfileMenuItemUi> = createProfileMenuItems(onAction = onAction)
    val cardBorderColor = Color(0xFFE4E8EF)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .border(
                width = 1.dp,
                color = cardBorderColor,
                shape = RoundedCornerShape(24.dp),
            ),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            menuItems.forEachIndexed { index, item ->
                ProfileMenuRow(
                    iconRes = item.iconRes,
                    titleRes = item.titleRes,
                    trailingTextRes = item.trailingTextRes,
                    onClick = item.onClick,
                    iconSize = iconSize,
                    titleFontSize = titleFontSize,
                    trailingFontSize = trailingFontSize,
                )
                if (index != menuItems.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = 1.dp,
                        color = cardBorderColor,
                    )
                }
            }
        }
    }
}
