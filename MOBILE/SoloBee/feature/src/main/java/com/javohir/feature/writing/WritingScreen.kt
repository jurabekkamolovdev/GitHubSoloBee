package com.javohir.feature.writing

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.javohir.feature.R
import com.javohir.feature.writing.content.PhoneContent
import com.javohir.feature.writing.content.TabletContent
import com.javohir.ui.component.AnimatedSnackBarHost
import kotlinx.coroutines.launch

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.writing
 * Description: Writing (trace) Composable Screen
 *
 * Spell rejimidan farqli o'laroq pull-to-refresh yo'q — vertikal tortish
 * chizish harakati bilan to'qnashadi.
 */
@Composable
fun WritingScreen(
    paddingValues: PaddingValues,
    viewModel: WritingViewModel = hiltViewModel(),
    isTablet: Boolean,
    onOpenWordHunt: (topicId: String, subCategoryName: String) -> Unit,
    onBackClick: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is WritingEvent.ShowError -> launch {
                    snackBarHostState.showSnackbar(event.message)
                }

                is WritingEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                is WritingEvent.PlayAudio -> {
                    try {
                        mediaPlayer?.release()
                        mediaPlayer = MediaPlayer().apply {
                            setAudioAttributes(
                                AudioAttributes.Builder()
                                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                    .setUsage(AudioAttributes.USAGE_MEDIA)
                                    .build()
                            )
                            setDataSource(event.url)
                            setOnPreparedListener { it.start() }
                            setOnCompletionListener {
                                it.release()
                                mediaPlayer = null
                            }
                            prepareAsync()
                        }
                    } catch (_: Exception) {
                        Toast.makeText(context, "Audio ijro etib bo'lmadi", Toast.LENGTH_SHORT).show()
                    }
                }

                is WritingEvent.NavigateToWordHunt -> {
                    onOpenWordHunt(event.topicId, event.subCategoryName)
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        WritingContent(
            paddingValues = paddingValues,
            isTablet = isTablet,
            state = state,
            onAction = viewModel::onAction,
            onBackClick = onBackClick,
        )
        AnimatedSnackBarHost(
            hostState = snackBarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(
                    bottom = paddingValues.calculateBottomPadding() + 12.dp,
                    start = 16.dp,
                    end = 16.dp
                )
        )
    }
}

@Composable
private fun WritingContent(
    paddingValues: PaddingValues,
    isTablet: Boolean,
    state: WritingState,
    onAction: (WritingIntent) -> Unit,
    onBackClick: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background_img),
            contentDescription = "Background image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        if (isTablet) {
            TabletContent(
                paddingValues = paddingValues,
                state = state,
                onAction = onAction,
                onBackClick = onBackClick,
            )
        } else {
            PhoneContent(
                paddingValues = paddingValues,
                state = state,
                onAction = onAction,
                onBackClick = onBackClick,
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun WritingPreview() {
    WritingContent(
        paddingValues = PaddingValues(all = 12.dp),
        isTablet = false,
        state = WritingState(char = "A", steps = WritingState.stepsOf(char = "A")),
        onAction = {},
        onBackClick = {},
    )
}
