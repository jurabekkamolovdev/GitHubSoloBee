package com.javohir.feature.picquest.content

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.picquest.content
 * Description: picquest/TabletContent: qurilma o'lchami bo'yicha Compose kontent.
 */


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.javohir.feature.R
import com.javohir.feature.common.ActivityTile
import com.javohir.feature.picquest.PicQuestIntent
import com.javohir.feature.picquest.PicQuestState
import com.javohir.ui.component.RemoteImage
import com.javohir.ui.theme.SoloBeeColors

@Composable
fun TabletContent(
    paddingValues: PaddingValues,
    state: PicQuestState,
    onAction: (PicQuestIntent) -> Unit,
    onBackClick: () -> Unit,
) {
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
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick, modifier = Modifier.size(56.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.back_btn),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(56.dp),
                    )
                }
                Text(
                    text = state.subCategoryName,
                    style = TextStyle(
                        fontSize = 26.sp,
                        color = SoloBeeColors.Black,
                        fontFamily = FontFamily(Font(R.font.nunito_reguler))
                    ),
                )
                Spacer(modifier = Modifier.weight(1f))
                Box(modifier = Modifier.height(52.dp), contentAlignment = Alignment.CenterStart) {
                    Box(
                        modifier = Modifier
                            .height(38.dp)
                            .background(SoloBeeColors.BadgeBackground, RoundedCornerShape(20.dp))
                            .padding(start = 56.dp, end = 14.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = state.score,
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

            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                state.activities.take(4).forEach { item ->
                    ActivityTile(
                        title = item.title,
                        completed = item.completed,
                        enabled = item.enabled,
                        modifier = Modifier.weight(1f),
                        tileSize = 94.dp,
                        backgroundImageSize = 104.dp,
                        iconSize = 38.dp,
                        iconOffsetY = (-14).dp,
                        titleOffsetY = 18.dp,
                        titleFontSize = 13.sp,
                        titleShimmerWidth = 62.dp,
                        titleShimmerHeight = 16.dp,
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = stringResource(id = R.string.picquest_choose_correct_image),
                style = TextStyle(
                    fontSize = 28.sp,
                    color = SoloBeeColors.Black,
                    fontFamily = FontFamily(Font(R.font.nunito_semibold)),
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )

            Spacer(modifier = Modifier.height(14.dp))

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.TopCenter,
            ) {
                val gridGap = 12.dp
                val gridWidth = maxWidth * 0.74f
                val cardSize = ((gridWidth - gridGap) / 2).coerceIn(130.dp, 190.dp)
                val options = state.options.take(4)

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(gridGap),
                ) {
                    options.chunked(2).forEach { row ->
                        Row(
                            modifier = Modifier.widthIn(max = gridWidth),
                            horizontalArrangement = Arrangement.spacedBy(gridGap),
                        ) {
                            row.forEach { option ->
                                Box(
                                    modifier = Modifier
                                        .size(cardSize)
                                        .clip(RoundedCornerShape(20.dp))
                                        .border(
                                            width = 1.dp,
                                            color = Color.White.copy(alpha = 0.45f),
                                            shape = RoundedCornerShape(20.dp),
                                        )
                                        .clickable { onAction(PicQuestIntent.OptionClicked(option)) },
                                ) {
                                    RemoteImage(
                                        imageUrl = option.imageUrl,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Fit,
                                        shape = RoundedCornerShape(20.dp),
                                    )
                                }
                            }
                            if (row.size == 1) {
                                Spacer(modifier = Modifier.size(cardSize))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Image(
                        painter = painterResource(id = R.drawable.audio_ic),
                        contentDescription = null,
                        modifier = Modifier
                            .size(96.dp)
                            .clickable { onAction(PicQuestIntent.PlayPromptAudio) },
                        contentScale = ContentScale.Fit,
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
