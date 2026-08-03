package com.javohir.feature.learn
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.javohir.feature.R
import com.javohir.feature.learn.content.PhoneContent
import com.javohir.feature.learn.content.TabletContent
import com.javohir.ui.component.AnimatedSnackBarHost
import kotlinx.coroutines.launch

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.learn
 * Description: Learn Composable Screen
 */


@Composable
fun LearnScreen(
    paddingValues: PaddingValues,
    viewModel: LearnViewModel = hiltViewModel(),
    isTablet: Boolean,
    onOpenWriting: (topicId: String, subCategoryName: String) -> Unit,
    onOpenTrace: (topicId: String, subCategoryName: String) -> Unit,
    onOpenWordHunt: (topicId: String, subCategoryName: String) -> Unit,
    onBackClick: () -> Unit,
){
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    val audioQueue = remember { ArrayDeque<LearnEvent.PlayAudio>() }
    var isAudioBusy by remember { mutableStateOf(false) }

    lateinit var playNextQueuedAudio: () -> Unit
    playNextQueuedAudio = {
        val nextAudio = audioQueue.removeFirstOrNull()
        if (nextAudio == null) {
            isAudioBusy = false
        } else {
            isAudioBusy = true
            try {
                mediaPlayer?.release()
                mediaPlayer = MediaPlayer().apply {
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
                    )
                    setDataSource(nextAudio.url)
                    setOnPreparedListener {
                        it.start()
                        viewModel.onAction(
                            LearnIntent.AudioPlayed(activityId = nextAudio.activityId)
                        )
                    }
                    setOnCompletionListener {
                        it.release()
                        mediaPlayer = null
                        playNextQueuedAudio()
                    }
                    setOnErrorListener { player, _, _ ->
                        player.release()
                        mediaPlayer = null
                        playNextQueuedAudio()
                        true
                    }
                    prepareAsync()
                }
            } catch (_: Exception) {
                mediaPlayer?.release()
                mediaPlayer = null
                playNextQueuedAudio()
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            audioQueue.clear()
            isAudioBusy = false
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }
    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when(event){
                is LearnEvent.ShowError ->{
                    launch {
                        snackBarHostState.showSnackbar(event.message)
                    }
                }
                is LearnEvent.ShowToast -> {
                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_SHORT,
                    ).show()
                }
                is LearnEvent.NavigateToWriting -> {
                    onOpenWriting(event.topicId, event.subCategoryName)
                }
                is LearnEvent.NavigateToTrace -> {
                    onOpenTrace(event.topicId, event.subCategoryName)
                }
                is LearnEvent.NavigateToWordHunt -> {
                    onOpenWordHunt(event.topicId, event.subCategoryName)
                }
                is LearnEvent.PlayAudio -> {
                    audioQueue.addLast(event)
                    if (!isAudioBusy) {
                        playNextQueuedAudio()
                    }
                }
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        LearnContent(
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LearnContent(
    paddingValues: PaddingValues,
    isTablet: Boolean,
    state: LearnState,
    onAction: (LearnIntent) -> Unit,
    onBackClick: () -> Unit,
){
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isRefreshing,
        onRefresh = {onAction(LearnIntent.Refresh)}
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(state = pullRefreshState)
    ) {
        Image(
            painter = painterResource(id = R.drawable.background_img),
            contentDescription = "Background image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        if (isTablet){
            TabletContent(
                paddingValues = paddingValues,
                state = state,
                onAction = onAction,
                onBackClick = onBackClick,
            )
        }else{
            PhoneContent(
                paddingValues = paddingValues,
                state = state,
                onAction = onAction,
                onBackClick = onBackClick,
            )
        }
        PullRefreshIndicator(
            refreshing = state.isRefreshing,
            state = pullRefreshState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = paddingValues.calculateTopPadding())
        )
    }
}

@Composable
@Preview(showBackground = true)
fun LearnPreview(){
    LearnContent(
      paddingValues = PaddingValues(all = 12.dp),
      isTablet = false,
      state = LearnState(),
      onAction = {},
      onBackClick = {},
    )
}