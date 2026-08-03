package com.javohir.feature.learn.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.javohir.feature.R
import com.javohir.feature.common.ActivityTile
import com.javohir.feature.learn.LearnIntent
import com.javohir.feature.learn.LearnState
import com.javohir.ui.component.RemoteImage
import com.javohir.ui.component.shimmerSkeleton
import com.javohir.ui.theme.SoloBeeColors

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.learn.content
 * Description: Tablet Content
 */
@Composable
fun TabletContent(
    paddingValues: PaddingValues,
    state: LearnState,
    onAction: (LearnIntent) -> Unit,
    onBackClick: () -> Unit,
){
    val tabletContentMaxWidth = 980.dp

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = tabletContentMaxWidth)
                .padding(top = paddingValues.calculateTopPadding())
                .padding(horizontal = 24.dp)
                .padding(bottom = paddingValues.calculateBottomPadding())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(56.dp)
                        .padding(end = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.back_btn),
                        contentDescription = "Back Button",
                        modifier = Modifier.size(56.dp),
                        tint = Color.Unspecified
                    )
                }
                Text(
                    text = if (state.isLoadingProfile) "" else state.subCategoryName,
                    style = TextStyle(
                        fontSize = 26.sp,
                        fontFamily = FontFamily(Font(R.font.nunito_reguler)),
                        color = SoloBeeColors.Black
                    ),
                    modifier = Modifier
                        .height(30.dp)
                        .fillMaxWidth(0.42f)
                        .shimmerSkeleton(
                            visible = state.isLoadingProfile,
                            shape = RoundedCornerShape(4.dp)
                        )
                )
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier.height(52.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Box(
                        modifier = Modifier
                            .height(38.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .shimmerSkeleton(
                                visible = state.isLoadingProfile,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .background(SoloBeeColors.BadgeBackground)
                            .border(
                                width = 1.dp,
                                color = SoloBeeColors.BadgeBorder,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(start = 56.dp, end = 14.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = if (state.isLoadingProfile) "" else state.score,
                            style = TextStyle(
                                fontSize = 26.sp,
                                color = SoloBeeColors.BadgeText,
                                fontFamily = FontFamily(Font(resId = R.font.baloo2_bold))
                            ),
                        )
                    }

                    Image(
                        painter = painterResource(id = R.drawable.trophy_ic),
                        contentDescription = null,
                        modifier = Modifier
                            .size(52.dp)
                            .align(Alignment.CenterStart)
                            .offset(x = (-6).dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                state.activities.take(4).forEach { item ->
                    ActivityTile(
                        title = item.title,
                        completed = item.completed,
                        enabled = item.enabled,
                        modifier = Modifier.weight(1f),
                        isLoadingTitle = state.isLoadingActivity,
                        tileSize = 94.dp,
                        backgroundImageSize = 102.dp,
                        iconSize = 38.dp,
                        iconOffsetY = (-14).dp,
                        titleOffsetY = 18.dp,
                        titleFontSize = 13.sp,
                        titleShimmerWidth = 62.dp,
                        titleShimmerHeight = 16.dp,
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                RemoteImage(
                    imageUrl = state.learnImageUrl,
                    modifier = Modifier
                        .fillMaxWidth(0.62f)
                        .aspectRatio(1.7f)
                        .clip(RoundedCornerShape(26.dp)),
                    contentScale = ContentScale.Crop,
                    shape = RoundedCornerShape(26.dp),
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.audio_ic),
                    contentDescription = "Sound",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .clickable { onAction(LearnIntent.PlayLearnAudio) }
                        .size(112.dp),
                    contentScale = ContentScale.Fit,
                )
                Image(
                    painter = painterResource(id = R.drawable.next_ic),
                    contentDescription = "Next",
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clickable { onAction(LearnIntent.OpenNext) }
                        .size(72.dp),
                    contentScale = ContentScale.Fit,
                )
            }
        }
    }
}