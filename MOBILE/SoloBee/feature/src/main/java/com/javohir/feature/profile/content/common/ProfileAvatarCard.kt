package com.javohir.feature.profile.content.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.javohir.feature.R
import com.javohir.ui.component.shimmerSkeleton
import com.javohir.ui.theme.SoloBeeColors

@Composable
internal fun ProfileAvatarCard(
    userInitial: String,
    fullName: String,
    isLoadingProfile: Boolean,
    onChangeAvatarClick: () -> Unit,
    modifier: Modifier = Modifier,
    avatarSize: Dp = 56.dp,
    avatarInitialFontSize: TextUnit = 36.sp,
    nameFontSize: TextUnit = 20.sp,
    changeIconSize: Dp = 20.dp,
    changeTextFontSize: TextUnit = 20.sp,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .border(
                width = 1.dp,
                color = Color(0xFFE4E8EF),
                shape = RoundedCornerShape(24.dp),
            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(avatarSize)
                    .clip(CircleShape)
                    .shimmerSkeleton(
                        visible = isLoadingProfile,
                        shape = CircleShape,
                    )
                    .background(SoloBeeColors.AvatarYellow),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = userInitial,
                    style = TextStyle(
                        fontSize = avatarInitialFontSize,
                        color = SoloBeeColors.White,
                        fontFamily = FontFamily(Font(resId = R.font.baloo2_bold)),
                    ),
                )
            }
            Text(
                text = if (isLoadingProfile) "" else fullName,
                style = TextStyle(
                    color = SoloBeeColors.Black,
                    fontSize = nameFontSize,
                    fontFamily = FontFamily(Font(R.font.nunito_semibold)),
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth(0.6f)
                    .height(24.dp)
                    .shimmerSkeleton(
                        visible = isLoadingProfile,
                        shape = RoundedCornerShape(6.dp),
                    ),
            )
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color(0xFFFFB92E))
                    .clickable(onClick = onChangeAvatarClick)
                    .padding(horizontal = 32.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.change_ic),
                    contentDescription = "Change avatar",
                    tint = Color.White,
                    modifier = Modifier.size(changeIconSize),
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(id = R.string.change),
                    style = TextStyle(
                        fontSize = changeTextFontSize,
                        color = Color.White,
                    ),
                )
            }
        }
    }
}
