package com.javohir.feature.topic

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.javohir.domain.common.Resource
import com.javohir.domain.model.Topic
import com.javohir.domain.useCase.LoadProfileWithTopicsUseCase
import com.javohir.utils.mvi.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.topic
 * Description: ViewModel
 */
@HiltViewModel
class TopicViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val loadProfileWithTopicsUseCase: LoadProfileWithTopicsUseCase,
) : BaseViewModel<TopicState, TopicEvent>(initialState = TopicState()) {

    private val navSubCategoryId: String? = savedStateHandle[SUB_CATEGORY_ID_ARG]
    private val navSubCategoryName: String? = savedStateHandle[SUB_CATEGORY_NAME_ARG]

    private var isFirstResume = true

    init {
        loadData()
    }

    fun onAction(intent: TopicIntent) {
        when (intent) {
            is TopicIntent.TopicClicked -> {
                viewModelScope.launch {
                    emitEvent(
                        TopicEvent.NavigateToLearn(
                            topicId = intent.topicId,
                            subCategoryName = navSubCategoryName.orEmpty(),
                        ),
                    )
                }
            }

            is TopicIntent.ScreenResumed -> {
                if (isFirstResume) isFirstResume = false else loadData()
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            val id = navSubCategoryId
            if (id.isNullOrBlank()) {
                emitEvent(TopicEvent.ShowError(message = "subCategoryId topilmadi"))
                updateState { state -> state.copy(isLoadingProfile = false, isLoadingTopics = false) }
                return@launch
            }

            updateState { state ->
                state.copy(
                    subCategoryId = id,
                    subCategoryName = navSubCategoryName.orEmpty(),
                    isLoadingProfile = true,
                    isLoadingTopics = true,
                )
            }

            val (profileResult, topicsResult) = loadProfileWithTopicsUseCase(
                subCategoryId = id,
                forceRefreshProfile = true,
            )

            when (profileResult) {
                is Resource.Success -> {
                    updateState { state -> state.copy(score = profileResult.data.score, isLoadingProfile = false) }
                }
                is Resource.Error -> {
                    emitEvent(TopicEvent.ShowError(message = profileResult.message))
                    updateState { state -> state.copy(isLoadingProfile = false) }
                }
                is Resource.Unauthorized -> {
                    updateState { state -> state.copy(isLoadingProfile = false) }
                }
                is Resource.Loading -> Unit
            }

            when (topicsResult) {
                is Resource.Success -> {
                    updateState { state ->
                        state.copy(
                            topics = topicsResult.data.map { it.toUi() },
                            isLoadingTopics = false,
                        )
                    }
                }
                is Resource.Error -> {
                    emitEvent(TopicEvent.ShowError(message = topicsResult.message))
                    updateState { state -> state.copy(isLoadingTopics = false) }
                }
                is Resource.Unauthorized -> Unit
                is Resource.Loading -> Unit
            }
        }
    }

    private fun Topic.toUi(): TopicItem {
        return TopicItem(
            id = id,
            subCategoryId = subCategoryId,
            imageUrl = imageUrl,
            enabled = enabled,
            completed = completed,
        )
    }

    private companion object {
        const val SUB_CATEGORY_ID_ARG = "subCategoryId"
        const val SUB_CATEGORY_NAME_ARG = "subCategoryName"
    }
}
