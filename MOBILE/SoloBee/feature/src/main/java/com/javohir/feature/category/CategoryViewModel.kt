package com.javohir.feature.category

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.javohir.domain.common.Resource
import com.javohir.domain.model.SubCategory
import com.javohir.domain.useCase.LoadProfileWithCategoriesUseCase
import com.javohir.utils.mvi.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.category
 * Description: ViewModel
 */
@HiltViewModel
class CategoryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val loadProfileWithCategoriesUseCase: LoadProfileWithCategoriesUseCase,
): BaseViewModel<CategoryState, CategoryEvent>(initialState = CategoryState()) {
    private val selectedCategoryId: String = savedStateHandle.get<String>(CATEGORY_ID_ARG).orEmpty()


    init {
        loadData()
    }

    fun onAction(intent: CategoryIntent){

        when(intent){
            is CategoryIntent.Refresh -> refreshData()
            is CategoryIntent.OpenTopics -> navigateTopics(
                subCategoryId = intent.subCategoryId,
                subCategoryName = intent.subCategoryName,
            )
            is CategoryIntent.NavigateToProfile -> {
                viewModelScope.launch {
                    emitEvent(CategoryEvent.NavigateToProfile)
                }
            }
        }
    }

    private fun navigateTopics(subCategoryId: String, subCategoryName: String) {
        viewModelScope.launch {
            val name = subCategoryName.trim()
            val isEnabled = ENABLED_SUB_CATEGORIES.any { it.equals(other = name, ignoreCase = true) }
            if (!isEnabled) {
                emitEvent(CategoryEvent.ShowToast(message = "Tez kunda"))
                return@launch
            }
            emitEvent(
                CategoryEvent.NavigateToTopics(
                    subCategoryId = subCategoryId,
                    subCategoryName = subCategoryName,
                ),
            )
        }
    }
    private fun loadData(){
        refreshInternal(showPullRefresh = false)
    }
    private fun refreshData(){
        refreshInternal(showPullRefresh = true)
    }
    private fun refreshInternal(showPullRefresh: Boolean) {
        viewModelScope.launch {
            updateState { state ->
                state.copy(
                isRefreshing = showPullRefresh,
                isLoadingProfile = true
            )
            }

            val (profileResult, categoriesResult) = loadProfileWithCategoriesUseCase(
                forceRefresh = showPullRefresh,
            )

            when (profileResult) {
                is Resource.Success -> {
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
                    emitEvent(event = CategoryEvent.ShowError(profileResult.message))
                    updateState { state -> state.copy(isLoadingProfile = false) }
                }
                is Resource.Unauthorized -> {
                    updateState { state -> state.copy(isLoadingProfile = false) }
                }

                is Resource.Loading -> Unit
            }

           when(categoriesResult){
               is Resource.Success -> {
                   val selectedCategory = categoriesResult.data.firstOrNull { it.id == selectedCategoryId } ?: categoriesResult.data.firstOrNull()

                   updateState { state ->
                       state.copy(
                       subCategories = selectedCategory?.subCategory?.map { it.toUi() }.orEmpty(),
                       footerName = selectedCategory?.name.orEmpty(),
                       footerLessonCount = "${selectedCategory?.lessonCount ?: 0} lessons",
                       footerBackgroundColor = selectedCategory?.backgroundColor?.toComposeColorOr(Color(0xFF009688)) ?: Color(0xFF009688),
                       footerForegroundColor = selectedCategory?.foregroundColor?.toComposeColorOr(Color(0xFF4CAF50)) ?: Color(0xFF4CAF50)
                   )
                   }
               }
               is Resource.Error -> {
                   emitEvent(event = CategoryEvent.ShowError(message = categoriesResult.message))
               }
               is Resource.Unauthorized -> Unit
               is Resource.Loading -> Unit
           }
            updateState { state -> state.copy(isRefreshing = false) }
        }
    }

    private fun SubCategory.toUi(): SubCategoryItem{
        return SubCategoryItem(
            id = id,
            categoryId = categoryId,
            image = imageUrl,
            subName = name
        )
    }
    private fun toInitial(name: String?): String {
        val cleaned = name.orEmpty().trim()
        return cleaned.firstOrNull()?.uppercaseChar()?.toString() ?: ""
    }
    private fun String.toComposeColorOr(default: Color): Color =
        runCatching { Color(this.toColorInt()) }.getOrDefault(default)

    private companion object {
        const val CATEGORY_ID_ARG = "categoryId"
        val ENABLED_SUB_CATEGORIES = listOf("alphabet", "colors","numbers")
    }
}