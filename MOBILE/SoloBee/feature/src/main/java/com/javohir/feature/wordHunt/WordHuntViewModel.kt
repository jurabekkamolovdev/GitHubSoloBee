package com.javohir.feature.wordHunt

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.javohir.domain.common.Resource
import com.javohir.domain.model.Activity
import com.javohir.domain.model.ActivityType
import com.javohir.domain.useCase.LoadProfileWithActivitiesUseCase
import com.javohir.domain.useCase.ProgressUseCase
import com.javohir.utils.mvi.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.wordhunt
 * Description: WordHuntViewModel: ekran UI holati, domain use case lar va hodisalar.
 */
@HiltViewModel
class WordHuntViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val loadProfileWithActivitiesUseCase: LoadProfileWithActivitiesUseCase,
    private val progressUseCase: ProgressUseCase,
) : BaseViewModel<WordHuntState, WordHuntEvent>(initialState = WordHuntState()) {

    private val navTopicId: String? = savedStateHandle[TOPIC_ID_ARG]
    private val navSubCategoryName: String? = savedStateHandle[SUB_CATEGORY_NAME_ARG]

    init {
        loadData(showRefresh = false)
    }

    fun onAction(intent: WordHuntIntent) {
        when (intent) {
            is WordHuntIntent.Refresh -> loadData(showRefresh = true)
            is WordHuntIntent.OptionClicked -> onOptionClicked(intent.option.isCorrect)
            is WordHuntIntent.PlayOptionAudio -> playOptionAudio(intent.url)
        }
    }

    private fun loadData(showRefresh: Boolean) {
        viewModelScope.launch {
            updateState { state ->
                state.copy(
                    topicId = navTopicId.orEmpty(),
                    subCategoryName = navSubCategoryName.orEmpty(),
                    isRefreshing = showRefresh,
                    isLoading = true,
                )
            }

            val topicId = navTopicId
            if (topicId.isNullOrBlank()) {
                emitEvent(WordHuntEvent.ShowError(message = "topicId topilmadi"))
                updateState { state -> state.copy(isRefreshing = false, isLoading = false) }
                return@launch
            }

            val (profileResult, activitiesResult) = loadProfileWithActivitiesUseCase(
                topicId = topicId,
                forceRefresh = showRefresh,
            )

            if (profileResult is Resource.Success) {
                updateState { state -> state.copy(score = profileResult.data.score) }
            } else if (profileResult is Resource.Error) {
                emitEvent(WordHuntEvent.ShowError(message = profileResult.message))
            }

            if (activitiesResult is Resource.Success) {
                val activityItems = activitiesResult.data.map { it.toUi() }
                val wordHunt = activitiesResult.data.firstOrNull { it.type == ActivityType.WORDHUNT }
                updateState { state ->
                    state.copy(
                        activities = activityItems,
                        activityId = wordHunt?.id.orEmpty(),
                        imageUrl = wordHunt?.payload?.imageUrl.orEmpty(),
                        options = wordHunt?.payload?.options.orEmpty(),
                        isRefreshing = false,
                        isLoading = false,
                    )
                }
            } else if (activitiesResult is Resource.Error) {
                emitEvent(WordHuntEvent.ShowError(message = activitiesResult.message))
                updateState { state -> state.copy(isRefreshing = false, isLoading = false) }
            }
        }
    }

    private fun playOptionAudio(url: String) {
        viewModelScope.launch {
            if (url.isBlank()) {
                emitEvent(WordHuntEvent.ShowToast(message = "Audio topilmadi"))
                return@launch
            }
            emitEvent(WordHuntEvent.PlayAudio(url = url))
        }
    }

    private fun onOptionClicked(isCorrect: Boolean) {
        viewModelScope.launch {
            val currentState = getCurrentState()
            val activityId = currentState.activityId
            if (activityId.isBlank()) {
                emitEvent(WordHuntEvent.ShowError(message = "Activity ID topilmadi"))
                return@launch
            }

            if (!isCorrect) {
                emitEvent(WordHuntEvent.ShowToast(message = "Noto'g'ri javob"))
                return@launch
            }

            val progressResult = progressUseCase(activityId = activityId).first { it !is Resource.Loading }
            if (progressResult is Resource.Error) {
                emitEvent(WordHuntEvent.ShowError(message = progressResult.message))
            } else {
                emitEvent(WordHuntEvent.ShowToast(message = "To'g'ri javob"))
                emitEvent(
                    WordHuntEvent.NavigateToPicQuest(
                        topicId = currentState.topicId,
                        subCategoryName = currentState.subCategoryName,
                    )
                )
            }
        }
    }

    private fun Activity.toUi(): WordHuntActivityItem {
        return WordHuntActivityItem(
            id = id,
            title = title,
            enabled = enabled,
            completed = completed,
        )
    }

    private companion object {
        const val TOPIC_ID_ARG = "topicId"
        const val SUB_CATEGORY_NAME_ARG = "subCategoryName"
    }
}
