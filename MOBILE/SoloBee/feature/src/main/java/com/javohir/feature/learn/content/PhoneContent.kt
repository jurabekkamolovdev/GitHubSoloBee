package com.javohir.feature.learn.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
 * Description: Phone Content
 */
@Composable
fun PhoneContent(
    paddingValues: PaddingValues,
    state: LearnState,
    onAction: (LearnIntent) -> Unit,
    onBackClick: () -> Unit,
){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = paddingValues.calculateTopPadding())
            .padding(all = 16.dp)
            .padding(bottom = paddingValues.calculateBottomPadding())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
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
                    fontSize = 22.sp,
                    fontFamily = FontFamily(Font(R.font.nunito_reguler)),
                    color = SoloBeeColors.Black
                ),
                modifier = Modifier
                    .height(24.dp)
                    .fillMaxWidth(0.35f)
                    .shimmerSkeleton(
                        visible = state.isLoadingProfile,
                        shape = RoundedCornerShape(1.dp)
                    )
            )
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier.height(44.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Box(
                    modifier = Modifier
                        .height(32.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shimmerSkeleton(
                            visible = state.isLoadingProfile,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .background(SoloBeeColors.BadgeBackground)
                        .border(
                            width = 1.dp,
                            color = SoloBeeColors.BadgeBorder,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(start = 46.dp, end = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = if (state.isLoadingProfile) "" else state.score,
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

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            state.activities.forEach { item ->
                ActivityTile(
                    title = item.title,
                    completed = item.completed,
                    enabled = item.enabled,
                    modifier = Modifier.weight(1f),
                    isLoadingTitle = state.isLoadingActivity,
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        RemoteImage(
            imageUrl = state.learnImageUrl,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(24.dp)),
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
                    .clickable { onAction(LearnIntent.PlayLearnAudio) }
                    .size(120.dp),
                contentScale = ContentScale.Fit,
            )
            Image(
                painter = painterResource(id = R.drawable.next_ic),
                contentDescription = "Next",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable { onAction(LearnIntent.OpenNext) }
                    .size(74.dp),
                contentScale = ContentScale.Fit,
            )
        }

    }
}

@Composable
@Preview
fun PhonePreview(){
    PhoneContent(
        paddingValues = PaddingValues(all = 12.dp),
        state = LearnState(),
        onAction = {},
        onBackClick = {},
    )
}