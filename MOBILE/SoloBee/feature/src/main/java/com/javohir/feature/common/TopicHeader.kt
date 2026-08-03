package com.javohir.feature.common

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.common
 * Description: Shared topic header (back button + title + score badge) reused by Writing screens.
 */

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
internal fun TopicHeader(
    subCategoryName: String,
    score: String,
    isLoadingProfile: Boolean,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    backButtonSize: Dp = 56.dp,
    titleFontSize: TextUnit = 22.sp,
    titleHeight: Dp = 24.dp,
    titleMaxWidthFraction: Float = 0.35f,
    titleShimmerCornerRadius: Dp = 1.dp,
    scoreRowHeight: Dp = 44.dp,
    scoreBadgeHeight: Dp = 32.dp,
    scoreBadgeCornerRadius: Dp = 16.dp,
    scoreBadgeStartPadding: Dp = 46.dp,
    scoreBadgeEndPadding: Dp = 12.dp,
    scoreFontSize: TextUnit = 22.sp,
    @DrawableRes trophyIconRes: Int = R.drawable.trophy_ic,
    trophySize: Dp = 44.dp,
    trophyOffsetX: Dp = (-4).dp,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(backButtonSize)
                .padding(end = 8.dp),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.back_btn),
                contentDescription = "Back Button",
                modifier = Modifier.size(backButtonSize),
                tint = Color.Unspecified,
            )
        }
        Text(
            text = if (isLoadingProfile) "" else subCategoryName,
            style = TextStyle(
                fontSize = titleFontSize,
                fontFamily = FontFamily(Font(R.font.nunito_reguler)),
                color = SoloBeeColors.Black,
            ),
            modifier = Modifier
                .height(titleHeight)
                .fillMaxWidth(titleMaxWidthFraction)
                .shimmerSkeleton(
                    visible = isLoadingProfile,
                    shape = RoundedCornerShape(titleShimmerCornerRadius),
                ),
        )
        Spacer(modifier = Modifier.weight(1f))
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
                    .padding(start = scoreBadgeStartPadding, end = scoreBadgeEndPadding),
                contentAlignment = Alignment.CenterStart,
            ) {
                Text(
                    text = if (isLoadingProfile) "" else score,
                    style = TextStyle(
                        fontSize = scoreFontSize,
                        color = SoloBeeColors.BadgeText,
                        fontFamily = FontFamily(Font(resId = R.font.baloo2_bold)),
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
