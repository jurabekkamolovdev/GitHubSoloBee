package com.javohir.feature.category
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import android.widget.Toast
import com.javohir.feature.R
import com.javohir.feature.category.content.PhoneContent
import com.javohir.feature.category.content.TabletContent
import com.javohir.ui.component.AnimatedSnackBarHost
import kotlinx.coroutines.launch

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.category
 * Description: Category Screen
 */
@Composable
fun CategoryScreen(
    paddingValues: PaddingValues,
    isTablet: Boolean,
    viewModel: CategoryViewModel = hiltViewModel(),
    onOpenTopics: (subCategoryId: String, subCategoryName: String) -> Unit,
    navigateToProfile: () -> Unit
){
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when(event){
                is CategoryEvent.ShowError ->{
                    launch {
                        snackBarHostState.showSnackbar(event.message)
                    }
                }
                is CategoryEvent.NavigateToTopics -> {
                    onOpenTopics(event.subCategoryId, event.subCategoryName)
                }
                is CategoryEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is CategoryEvent.NavigateToProfile -> {
                    navigateToProfile()
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        CategoryContent(
            paddingValues = paddingValues,
            isTablet = isTablet,
            state = state,
            onAction = viewModel::onAction,
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
fun CategoryContent(
    paddingValues: PaddingValues,
    isTablet: Boolean,
    state: CategoryState,
    onAction: (CategoryIntent) -> Unit,
){
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isRefreshing,
        onRefresh = {onAction(CategoryIntent.Refresh)}
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

        if (isTablet){
            TabletContent(
                paddingValues = paddingValues,
                state = state,
                onAction = onAction,
            )
        }else{
            PhoneContent(
                paddingValues = paddingValues,
                state = state,
                onAction = onAction,
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
@Preview(showBackground = true, device = "id:pixel_9")
fun CategoryPreview(){
    CategoryContent(
        paddingValues = PaddingValues(all = 12.dp),
        isTablet = true,
        state = CategoryState(),
        onAction = {},
    )
}



