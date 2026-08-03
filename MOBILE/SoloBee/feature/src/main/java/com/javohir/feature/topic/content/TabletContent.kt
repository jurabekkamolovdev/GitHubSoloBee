package com.javohir.feature.topic.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.javohir.feature.topic.TopicIntent
import com.javohir.feature.topic.TopicState
import com.javohir.feature.topic.tileBackgroundResId
import com.javohir.ui.component.TopicTile
import com.javohir.ui.component.shimmerSkeleton
import com.javohir.ui.theme.SoloBeeColors

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.topic.content
 * Description: Tablet Content
 */

@Composable
fun TabletContent(
    paddingValues: PaddingValues,
    state: TopicState,
    onAction: (TopicIntent) -> Unit,
    onBackClick: () -> Unit,
) {
    val columns = 5
    val rows = state.topics.chunked(columns)

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 980.dp)
                .padding(top = paddingValues.calculateTopPadding())
                .padding(bottom = paddingValues.calculateBottomPadding())
                .padding(horizontal = 24.dp),
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
                        contentDescription = null,
                        modifier = Modifier.size(56.dp),
                        tint = Color.Unspecified
                    )
                }
                Text(
                    text = if (state.isLoading) "" else state.subCategoryName,
                    style = TextStyle(
                        fontSize = 26.sp,
                        fontFamily = FontFamily(Font(R.font.nunito_reguler)),
                        color = SoloBeeColors.Black
                    ),
                    modifier = Modifier
                        .height(30.dp)
                        .fillMaxWidth(0.42f)
                        .shimmerSkeleton(
                            visible = state.isLoading,
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
                                visible = state.isLoading,
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
                            text = if (state.isLoading) "" else state.score,
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

            Spacer(modifier = Modifier.height(16.dp))

            when {
                state.isLoadingTopics && state.topics.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .widthIn(max = 760.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                            Box(
                                modifier = Modifier
                                .fillMaxWidth(0.92f)
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(20.dp))
                                .shimmerSkeleton(
                                    visible = true,
                                    shape = RoundedCornerShape(20.dp),
                                ),
                            )
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .widthIn(max = 760.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(
                            items = rows,
                            key = { row -> row.joinToString(separator = "|") { it.id } },
                        ) { rowItems ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                rowItems.forEach { item ->
                                    TopicTile(
                                        tileBackgroundResId = item.tileBackgroundResId(),
                                        imageUrl = item.imageUrl,
                                        clickable = item.enabled,
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(1f),
                                        imagePaddingBottom = 10.dp,
                                        imagePaddingEnd = 10.dp,
                                        onClick = {
                                            onAction(TopicIntent.TopicClicked(topicId = item.id))
                                        },
                                    )
                                }
                                repeat(columns - rowItems.size) {
                                    Spacer(
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(1f),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
