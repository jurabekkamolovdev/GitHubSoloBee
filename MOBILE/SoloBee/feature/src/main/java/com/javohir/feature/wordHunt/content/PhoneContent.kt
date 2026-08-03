package com.javohir.feature.wordHunt.content

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.wordhunt.content
 * Description: wordHunt/PhoneContent: qurilma o'lchami bo'yicha Compose kontent.
 */

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
fun PhoneContent(
    paddingValues: PaddingValues,
    state: WordHuntState,
    onAction: (WordHuntIntent) -> Unit,
    onBackClick: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background_img),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
                .padding(horizontal = 16.dp),
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
                        tint = Color.Unspecified,
                    )
                }
                Text(
                    text = state.subCategoryName,
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontFamily = FontFamily(Font(R.font.nunito_reguler)),
                        color = SoloBeeColors.Black
                    ),
                )
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier.height(44.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Box(
                        modifier = Modifier
                            .height(32.dp)
                            .background(SoloBeeColors.BadgeBackground, RoundedCornerShape(16.dp))
                            .border(1.dp, SoloBeeColors.BadgeBorder, RoundedCornerShape(16.dp))
                            .padding(start = 46.dp, end = 12.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = state.score,
                            style = TextStyle(
                                fontSize = 22.sp,
                                color = SoloBeeColors.BadgeText,
                                fontFamily = FontFamily(Font(resId = R.font.baloo2_bold))
                            ),
                        )
                    }
                    Image(
                        painter = painterResource(id = R.drawable.trophy_ic),
                        contentDescription = null,
                        modifier = Modifier
                            .size(44.dp)
                            .align(Alignment.CenterStart)
                            .offset(x = (-4).dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                state.activities.take(4).forEach { item ->
                    ActivityTile(
                        title = item.title,
                        completed = item.completed,
                        enabled = item.enabled,
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            RemoteImage(
                imageUrl = state.imageUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(24.dp)),
                contentScale = ContentScale.Fit,
                shape = RoundedCornerShape(24.dp),
            )

            Spacer(modifier = Modifier.weight(1f))

            state.options.forEach { option ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    RemoteImage(
                        imageUrl = option.imageUrl,
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .clip(RoundedCornerShape(28.dp))
                            .clickable { onAction(WordHuntIntent.OptionClicked(option)) },
                    )
                    Image(
                        painter = painterResource(id = R.drawable.audio_ic),
                        contentDescription = null,
                        modifier = Modifier
                            .size(56.dp)
                            .clickable {
                                onAction(
                                    WordHuntIntent.PlayOptionAudio(url = option.audioUrl.orEmpty())
                                )
                            },
                        contentScale = ContentScale.Fit
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
