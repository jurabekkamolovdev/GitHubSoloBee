package com.javohir.feature.topic
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.javohir.feature.R
import com.javohir.feature.topic.content.PhoneContent
import com.javohir.feature.topic.content.TabletContent
import com.javohir.ui.component.AnimatedSnackBarHost
import kotlinx.coroutines.launch

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.topic
 * Description: Topic Screen
 */
@Composable
fun TopicScreen(
    paddingValues: PaddingValues,
    viewModel: TopicViewModel = hiltViewModel(),
    isTablet: Boolean,
    onOpenLearn: (topicId: String, subCategoryName: String) -> Unit,
    onBackClick: () -> Unit,
){
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }


    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.onAction(TopicIntent.ScreenResumed)
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is TopicEvent.ShowError -> launch {
                    snackBarHostState.showSnackbar(message = event.message)
                }
                is TopicEvent.NavigateToLearn -> {
                    onOpenLearn(event.topicId, event.subCategoryName)
                }
            }
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
    ) {
        TopicContent(
            paddingValues = paddingValues,
            state = state,
            onAction = viewModel::onAction,
            isTablet = isTablet,
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
fun TopicContent(
     paddingValues: PaddingValues,
     state: TopicState,
     onAction: (TopicIntent) -> Unit,
     isTablet: Boolean,
     onBackClick: () -> Unit,
){

    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
       Image(
          painter = painterResource(id = R.drawable.background_img),
           contentDescription = null,
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
    }
}
@Composable
@Preview
fun TopicPreview(){
    TopicContent(
        paddingValues = PaddingValues(all = 12.dp),
        state = TopicState(),
        onAction = {},
        isTablet = false,
        onBackClick = {},
    )
}