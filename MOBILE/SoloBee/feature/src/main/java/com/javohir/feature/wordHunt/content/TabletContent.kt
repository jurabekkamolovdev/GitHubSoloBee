package com.javohir.feature.wordHunt.content

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.wordhunt.content
 * Description: wordHunt/TabletContent: qurilma o'lchami bo'yicha Compose kontent.
 */

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.javohir.feature.R
import com.javohir.feature.common.ActivityTile
import com.javohir.feature.wordHunt.WordHuntIntent
import com.javohir.feature.wordHunt.WordHuntState
import com.javohir.ui.component.RemoteImage
import com.javohir.ui.theme.SoloBeeColors
@Composable
fun TabletContent(
    paddingValues: PaddingValues,
    state: WordHuntState,
    onAction: (WordHuntIntent) -> Unit,
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

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.TopCenter,
            ) {
                BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                    val optionHeight = 56.dp
                    val optionGap = 8.dp
                    val blockGap = 12.dp
                    val optionsCount = state.options.take(4).size
                    val optionsBlockHeight =
                        if (optionsCount == 0) 0.dp
                        else (optionHeight * optionsCount) + (optionGap * (optionsCount - 1))
                    val imageHeight = (maxHeight - optionsBlockHeight - blockGap).coerceIn(140.dp, 250.dp)

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(blockGap),
                    ) {
                        RemoteImage(
                            imageUrl = state.imageUrl,
                            modifier = Modifier
                                .fillMaxWidth(0.58f)
                                .height(imageHeight)
                                .clip(RoundedCornerShape(24.dp)),
                            contentScale = ContentScale.Fit,
                            shape = RoundedCornerShape(24.dp),
                        )

                        state.options.take(4).forEachIndexed { index, option ->
                            Row(
                                modifier = Modifier.fillMaxWidth(0.52f),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                RemoteImage(
                                    imageUrl = option.imageUrl,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(optionHeight)
                                        .clip(RoundedCornerShape(28.dp))
                                        .clickable { onAction(WordHuntIntent.OptionClicked(option)) },
                                    contentScale = ContentScale.Crop,
                                    shape = RoundedCornerShape(28.dp),
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.audio_ic),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clickable {
                                            onAction(WordHuntIntent.PlayOptionAudio(option.audioUrl.orEmpty()))
                                        },
                                    contentScale = ContentScale.Fit
                                )
                            }
                            if (index != optionsCount - 1) {
                                Spacer(modifier = Modifier.height(optionGap))
                            }
                        }
                    }
                }
            }
        }
    }
}
