package com.javohir.feature.home
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import androidx.lifecycle.viewModelScope
import com.javohir.domain.common.Resource
import com.javohir.domain.model.Category
import com.javohir.domain.useCase.LoadProfileWithCategoriesUseCase
import com.javohir.utils.mvi.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.home
 * Description: ViewModel 
 */

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val loadProfileWithCategoriesUseCase: LoadProfileWithCategoriesUseCase,
) : BaseViewModel<HomeState, HomeEvent>(initialState = HomeState()) {

    init {
        loadData()
    }

    fun onAction(intent: HomeIntent){
        when(intent){
            HomeIntent.Refresh -> refreshData()
            is HomeIntent.CategoryClicked -> categoryClicked(intent.categoryId)
            is HomeIntent.NavigateToProfile -> {
                viewModelScope.launch {
                    emitEvent(HomeEvent.NavigateToProfile)
                }
            }
        }
    }

    private fun categoryClicked(categoryId: String){
        viewModelScope.launch {
            emitEvent(HomeEvent.NavigateToCategories(categoryId = categoryId))
        }
    }
    private fun loadData(){
        refreshInternal(showPullRefresh = false)
    }

    private fun refreshData(){
        refreshInternal(showPullRefresh = true)
    }

    private fun refreshInternal(showPullRefresh: Boolean){
      viewModelScope.launch {
            updateState { state ->
                state.copy(
                isRefreshing = showPullRefresh,
                isLoadingProfile = true,
                isLoadingCategories = true
            )
            }
            val (profileResult, categoriesResult) = loadProfileWithCategoriesUseCase(
                forceRefresh = showPullRefresh,
            )

            when(profileResult){
                is Resource.Success ->{
                    val firstName = profileResult.data.firstName
                    val score = profileResult.data.score
                    updateState { state ->
                        state.copy(
                        firstName = firstName,
                        userInitial = toInitial(name = firstName),
                        score = score,
                        isLoadingProfile = false
                    )
                    }
                }
                is Resource.Error -> {
                    emitEvent(event = HomeEvent.ShowError(profileResult.message))
                    updateState { state -> state.copy(isLoadingProfile = false) }
                }
                is Resource.Unauthorized -> {
                    updateState { state -> state.copy(isLoadingProfile = false) }
                }
                is Resource.Loading -> Unit

            }

            when(categoriesResult){
                is Resource.Success ->{
                    updateState { state ->
                        state.copy(
                        categories = categoriesResult.data.map { it.toUi() },
                        isLoadingCategories = false
                    )
                    }
                }
                is Resource.Error -> {
                    emitEvent(event = HomeEvent.ShowError(categoriesResult.message))
                    updateState { state -> state.copy(isLoadingCategories = false) }
                }
                is Resource.Unauthorized -> Unit
                is Resource.Loading -> Unit
            }
            updateState { state -> state.copy(isRefreshing = false) }
        }
    }
    private fun Category.toUi(): CategoryItem {
        return CategoryItem(
            id = id,
            foregroundColor = foregroundColor.toComposeColorOr(Color(0xFF4CAF50)),
            backgroundColor = backgroundColor.toComposeColorOr(Color(0xFF009688)),
            name = name,
            lessonCount = "$lessonCount lessons",
            images = images
        )
    }
    private fun String.toComposeColorOr(default: Color): Color =
        runCatching { Color(this.toColorInt()) }.getOrDefault(default)

    private fun toInitial(name: String?): String {
        val cleaned = name.orEmpty().trim()
        return cleaned.firstOrNull()?.uppercaseChar()?.toString() ?: ""
    }
}