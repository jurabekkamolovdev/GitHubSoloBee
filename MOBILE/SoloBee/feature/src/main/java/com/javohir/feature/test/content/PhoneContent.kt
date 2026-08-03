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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
 * Description: Phone Content
 */
@Composable
fun PhoneContent(
    paddingValues: PaddingValues,
    state: TestState,
    onAction: (TestIntent) -> Unit,
    onBackClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = paddingValues.calculateTopPadding())
            .padding(all = 16.dp)
            .padding(bottom = paddingValues.calculateBottomPadding())
    ) {
        TopicHeader(
            subCategoryName = state.subCategoryName,
            score = state.score,
            isLoadingProfile = state.isLoadingProfile,
            onBackClick = onBackClick,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            state.activities.take(4).forEach { item ->
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
            imageUrl = state.imageUrl,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(24.dp)),
            contentScale = ContentScale.Fit,
            shape = RoundedCornerShape(24.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        TypedWordRow(state = state, onAction = onAction)

        Spacer(modifier = Modifier.height(16.dp))

        LetterOptionsGrid(state = state, onAction = onAction)

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
                    .size(120.dp),
                contentScale = ContentScale.Fit,
            )
            Image(
                painter = painterResource(id = R.drawable.next_ic),
                contentDescription = "Next",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable { onAction(TestIntent.Submit) }
                    .size(74.dp),
                contentScale = ContentScale.Fit,
            )
        }
    }
}