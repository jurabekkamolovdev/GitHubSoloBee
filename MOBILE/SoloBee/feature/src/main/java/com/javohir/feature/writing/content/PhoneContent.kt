package com.javohir.feature.writing.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.javohir.feature.R
import com.javohir.feature.common.ActivityTile
import com.javohir.feature.common.TopicHeader
import com.javohir.feature.writing.WritingIntent
import com.javohir.feature.writing.WritingState
import com.javohir.feature.writing.trace.TraceCanvas

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.writing.content
 * Description: Phone Content
 */
@Composable
fun PhoneContent(
    paddingValues: PaddingValues,
    state: WritingState,
    onAction: (WritingIntent) -> Unit,
    onBackClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
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

        TraceStepCanvas(
            state = state,
            onAction = onAction,
            modifier = Modifier
                .fillMaxWidth()
                .weight(4f),
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
                    .clickable { onAction(WritingIntent.PlayCharAudio) }
                    .size(120.dp),
                contentScale = ContentScale.Fit,
            )
            Image(
                painter = painterResource(id = R.drawable.next_ic),
                contentDescription = "Next",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable { onAction(WritingIntent.Submit) }
                    .size(74.dp),
                contentScale = ContentScale.Fit,
            )
        }
    }
}

/**
 * Joriy qadamning chizish maydoni. Barcha qadamlar tugagach oxirgi belgi
 * to'liq chizilgan holida qoladi — foydalanuvchi next'ni bosishini kutamiz.
 */
@Composable
internal fun TraceStepCanvas(
    state: WritingState,
    onAction: (WritingIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val char = state.currentChar ?: state.steps.lastOrNull() ?: return
    // attempt o'zgarsa chizish noldan boshlanadi. Bitta qadamli belgilarda (sonlar)
    // char o'zgarmagani uchun buni faqat shu kalit uzadi.
    key(state.attempt) {
        TraceCanvas(
            char = char,
            modifier = modifier,
            onCompleted = { onAction(WritingIntent.TraceFinished) },
        )
    }
}
