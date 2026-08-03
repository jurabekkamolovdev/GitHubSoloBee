package com.javohir.feature.home
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.javohir.feature.R
import com.javohir.feature.home.content.PhoneContent
import com.javohir.feature.home.content.TabletContent
import com.javohir.ui.component.AnimatedSnackBarHost
import kotlinx.coroutines.launch

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.home
 * Description: Home Composable Screen
 */

@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    isTablet: Boolean,
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToCategory: (String) -> Unit,
    navigateToProfile: () -> Unit
){

    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    
    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when(event){
                is HomeEvent.ShowError -> {
                    launch {
                        snackBarHostState.showSnackbar(event.message)
                    }
                }
                is HomeEvent.NavigateToCategories -> {
                    navigateToCategory(event.categoryId)
                }
                is HomeEvent.NavigateToProfile ->{
                    navigateToProfile()
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        HomeContent(
            paddingValues = paddingValues,
            isTablet = isTablet,
            state = state,
            onAction = viewModel::onAction
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
fun HomeContent(
    paddingValues: PaddingValues,
    isTablet: Boolean,
    state: HomeState,
    onAction: (HomeIntent) -> Unit
){
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isRefreshing,
        onRefresh = { onAction(HomeIntent.Refresh) }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        Image(
            painter = painterResource(id = R.drawable.background_img),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
                .padding(all = 16.dp)
        ) {
            if (isTablet) {
                TabletContent(
                    paddingValues = paddingValues,
                    state = state,
                    onAction = onAction
                )
            } else {
                PhoneContent(
                    paddingValues = paddingValues,
                    state = state,
                    onAction = onAction
                )
            }
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
@Preview()
fun HomePreview(){
    HomeContent(
        paddingValues = PaddingValues(all = 12.dp),
        isTablet = false,
        state = HomeState(),
        onAction = {}
    )
}

