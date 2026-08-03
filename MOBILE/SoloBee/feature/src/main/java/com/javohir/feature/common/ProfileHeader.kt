package com.javohir.feature.common

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.common
 * Description: Shared profile header (avatar + greeting + score badge) reused by Home and Category.
 */

import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.javohir.feature.R
import com.javohir.ui.component.shimmerSkeleton
import com.javohir.ui.theme.SoloBeeColors

@Composable
internal fun ProfileHeader(
    userInitial: String,
    firstName: String,
    score: String,
    isLoadingProfile: Boolean,
    modifier: Modifier = Modifier,
    navigateToProfile: () -> Unit,
    avatarSize: Dp = 44.dp,
    avatarInitialFontSize: TextUnit = 28.sp,
    greetingNameSpacing: Dp = 12.dp,
    firstNameMaxWidthFraction: Float = 0.35f,
    firstNameHeight: Dp = 20.dp,
    scoreRowHeight: Dp = 44.dp,
    scoreBadgeHeight: Dp = 32.dp,
    scoreBadgeCornerRadius: Dp = 16.dp,
    scoreBadgeStartPadding: Dp = 46.dp,
    scoreFontSize: TextUnit = 22.sp,
    @FontRes scoreFontRes: Int = R.font.baloo2_bold,
    @DrawableRes trophyIconRes: Int = R.drawable.trophy_ic,
    trophySize: Dp = 44.dp,
    trophyOffsetX: Dp = (-4).dp,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(avatarSize)
                    .clip(CircleShape)
                    .clickable{navigateToProfile()}
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
            Spacer(modifier = Modifier.width(greetingNameSpacing))
            Column {
                Text(
                    text = stringResource(id = R.string.good_afternoon),
                    style = TextStyle(
                        color = SoloBeeColors.TextMuted,
                        fontFamily = FontFamily(Font(resId = R.font.nunito_reguler)),
                    ),
                )
                Text(
                    text = if (isLoadingProfile) "" else firstName,
                    style = TextStyle(
                        color = SoloBeeColors.Black,
                        fontFamily = FontFamily(Font(resId = R.font.nunito_semibold)),
                    ),
                    modifier = Modifier
                        .fillMaxWidth(firstNameMaxWidthFraction)
                        .height(firstNameHeight)
                        .shimmerSkeleton(
                            visible = isLoadingProfile,
                            shape = RoundedCornerShape(6.dp),
                        ),
                )
            }
        }
        Box(
            modifier = Modifier.height(scoreRowHeight),
            contentAlignment = Alignment.CenterStart,
        ) {
            Box(
                modifier = Modifier
                    .height(scoreBadgeHeight)
                    .clip(RoundedCornerShape(scoreBadgeCornerRadius))
                    .shimmerSkeleton(
                        visible = isLoadingProfile,
                        shape = RoundedCornerShape(scoreBadgeCornerRadius),
                    )
                    .background(SoloBeeColors.BadgeBackground)
                    .border(
                        width = 1.dp,
                        color = SoloBeeColors.BadgeBorder,
                        shape = RoundedCornerShape(scoreBadgeCornerRadius),
                    )
                    .padding(start = scoreBadgeStartPadding, end = 12.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                Text(
                    text = if (isLoadingProfile) "" else score,
                    style = TextStyle(
                        fontSize = scoreFontSize,
                        color = SoloBeeColors.BadgeText,
                        fontFamily = FontFamily(Font(resId = scoreFontRes)),
                    ),
                )
            }
            Image(
                painter = painterResource(id = trophyIconRes),
                contentDescription = null,
                modifier = Modifier
                    .size(trophySize)
                    .align(Alignment.CenterStart)
                    .offset(x = trophyOffsetX),
                contentScale = ContentScale.Fit,
            )
        }
    }
}
