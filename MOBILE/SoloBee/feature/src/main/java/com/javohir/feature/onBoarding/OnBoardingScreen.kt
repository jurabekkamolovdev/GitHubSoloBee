package com.javohir.feature.onBoarding
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.javohir.feature.R
import com.javohir.feature.onBoarding.content.PhoneContent
import com.javohir.feature.onBoarding.content.TabletContent
import kotlinx.coroutines.flow.collectLatest

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.onBoarding
 * Description: OnBoarding Screen
 */

@Composable
fun OnBoardingScreen(
    paddingValues: PaddingValues,
    isTablet: Boolean,
    viewModel: OnBoardingViewModel = hiltViewModel(),
    navigateToWelcome: () -> Unit,
){
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when(event){
                OnBoardingEvent.NavigateToWelcome -> navigateToWelcome()
            }
        }
    }

    OnBoardingContent(
        paddingValues = paddingValues,
        isTablet = isTablet,
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun OnBoardingContent(
    paddingValues: PaddingValues,
    isTablet: Boolean,
    state: OnBoardingState,
    onAction: (OnBoardingIntent) -> Unit
) {

    val pagerState = rememberPagerState(
        pageCount = { state.pages.size },
        initialPage = state.currentPage
    )

    LaunchedEffect(state.currentPage) {
        if (state.currentPage != pagerState.currentPage) {
            pagerState.animateScrollToPage(state.currentPage)
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collectLatest { page ->
            if (page != state.currentPage) {
                onAction(OnBoardingIntent.PageChanged(page))
            }
        }
    }

    val currentPage = state.pages[pagerState.currentPage]

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { pageIndex ->
            val page = state.pages[pageIndex]
            Image(
                painter = painterResource(page.image),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = if (isTablet) ContentScale.FillWidth else ContentScale.Crop
            )
        }

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isTablet) {
                TabletContent(
                    currentPage = currentPage,
                    pageCount = state.pages.size,
                    currentPageIndex = pagerState.currentPage,
                    maxHeight = maxHeight,
                    onAction = onAction
                )
            } else {
                PhoneContent(
                    currentPage = currentPage,
                    pageCount = state.pages.size,
                    currentPageIndex = pagerState.currentPage,
                    maxHeight = maxHeight,
                    onAction = onAction
                )
            }
        }
    }
}

@Composable
@Preview(device = "id:Nexus 10")
fun OnBoardingPreview(){
    OnBoardingContent(
        paddingValues = PaddingValues(all = 12.dp),
        isTablet = false,
        state = OnBoardingState(),
        onAction = {}
    )
}