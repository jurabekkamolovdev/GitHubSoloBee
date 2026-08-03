package com.javohir.feature.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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

@DrawableRes
fun activityBackgroundResId(completed: Boolean, enabled: Boolean): Int {
    return when {
        completed -> R.drawable.completed_activity
        enabled -> R.drawable.enabled_activity
        else -> R.drawable.disabled_activity
    }
}

@DrawableRes
fun activityIconResId(title: String): Int {
    return when (title.uppercase()) {
        "LEARN" -> R.drawable.learn_img
        "WRITING" -> R.drawable.writing_img
        "WORDHUNT" -> R.drawable.wordhunt_img
        "PICQUEST" -> R.drawable.picquest_img
        else -> R.drawable.learn_img
    }
}

@Composable
fun ActivityTile(
    title: String,
    completed: Boolean,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    isLoadingTitle: Boolean = false,
    tileSize: Dp = 83.dp,
    backgroundImageSize: Dp = 94.dp,
    iconSize: Dp = 34.dp,
    iconOffsetY: Dp = (-12).dp,
    titleOffsetY: Dp = 16.dp,
    titleFontSize: TextUnit = 12.sp,
    titleShimmerWidth: Dp = 56.dp,
    titleShimmerHeight: Dp = 14.dp,
) {
    Box(
        modifier = modifier.size(tileSize),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = activityBackgroundResId(completed, enabled)),
            contentDescription = null,
            modifier = Modifier.size(backgroundImageSize),
            contentScale = ContentScale.Crop,
        )
        Image(
            painter = painterResource(id = activityIconResId(title)),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = iconOffsetY)
                .size(iconSize),
            contentScale = ContentScale.Crop,
        )
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = titleOffsetY)
                .size(width = titleShimmerWidth, height = titleShimmerHeight)
                .shimmerSkeleton(
                    visible = isLoadingTitle,
                    shape = RoundedCornerShape(8.dp),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = if (isLoadingTitle) "" else title,
                maxLines = 1,
                style = TextStyle(
                    fontSize = titleFontSize,
                    color = SoloBeeColors.White,
                    fontFamily = FontFamily(Font(R.font.nunito_semibold)),
                    textAlign = TextAlign.Center,
                ),
            )
        }
    }
}
