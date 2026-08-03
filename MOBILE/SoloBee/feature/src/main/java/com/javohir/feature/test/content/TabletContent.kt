package com.javohir.feature.test.content

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.javohir.feature.R
import com.javohir.feature.common.ActivityTile
import com.javohir.feature.common.TopicHeader
import com.javohir.feature.test.TestIntent
import com.javohir.feature.test.TestState
import com.javohir.ui.component.RemoteImage

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.test.content
 * Description: Tablet Content
 */
@Composable
fun TabletContent(
    paddingValues: PaddingValues,
    state: TestState,
    onAction: (TestIntent) -> Unit,
    onBackClick: () -> Unit,
) {
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
            TopicHeader(
                subCategoryName = state.subCategoryName,
                score = state.score,
                isLoadingProfile = state.isLoadingProfile,
                onBackClick = onBackClick,
                titleFontSize = 26.sp,
                titleHeight = 30.dp,
                titleMaxWidthFraction = 0.42f,
                titleShimmerCornerRadius = 4.dp,
                scoreRowHeight = 52.dp,
                scoreBadgeHeight = 38.dp,
                scoreBadgeCornerRadius = 20.dp,
                scoreBadgeStartPadding = 56.dp,
                scoreBadgeEndPadding = 14.dp,
                scoreFontSize = 26.sp,
                trophySize = 52.dp,
                trophyOffsetX = (-6).dp,
            )

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
                    imageUrl = state.imageUrl,
                    modifier = Modifier
                        .fillMaxWidth(0.55f)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(26.dp)),
                    contentScale = ContentScale.Crop,
                    shape = RoundedCornerShape(26.dp),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TypedWordRow(state = state, onAction = onAction, slotSize = 52.dp, fontSize = 32.sp)

            Spacer(modifier = Modifier.height(16.dp))

            LetterOptionsGrid(
                state = state,
                onAction = onAction,
                columns = 6,
            )

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.audio_ic),
                    contentDescription = "Sound",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .clickable { onAction(TestIntent.PlayWordAudio) }
                        .size(112.dp),
                    contentScale = ContentScale.Fit,
                )
                Image(
                    painter = painterResource(id = R.drawable.next_ic),
                    contentDescription = "Next",
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clickable { onAction(TestIntent.Submit) }
                        .size(72.dp),
                    contentScale = ContentScale.Fit,
                )
            }
        }
    }
}