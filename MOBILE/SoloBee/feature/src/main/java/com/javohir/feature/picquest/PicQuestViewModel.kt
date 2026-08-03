package com.javohir.feature.picquest

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
 * Package: com.javohir.feature.picquest
 * Description: PicQuestViewModel: ekran UI holati, domain use case lar va hodisalar.
 */

@HiltViewModel
class PicQuestViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val loadProfileWithActivitiesUseCase: LoadProfileWithActivitiesUseCase,
    private val progressUseCase: ProgressUseCase,
) : BaseViewModel<PicQuestState, PicQuestEvent>(initialState = PicQuestState()) {

    private val navTopicId: String? = savedStateHandle[TOPIC_ID_ARG]
    private val navSubCategoryName: String? = savedStateHandle[SUB_CATEGORY_NAME_ARG]

    init {
        loadData(showRefresh = false)
    }

    fun onAction(intent: PicQuestIntent) {
        when (intent) {
            is PicQuestIntent.Refresh -> loadData(showRefresh = true)
            is PicQuestIntent.PlayPromptAudio -> playPromptAudio()
            is PicQuestIntent.OptionClicked -> onOptionClicked(option = intent.option)
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
                emitEvent(PicQuestEvent.ShowError(message = "topicId topilmadi"))
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
                emitEvent(PicQuestEvent.ShowError(message = profileResult.message))
            }

            if (activitiesResult is Resource.Success) {
                val activityItems = activitiesResult.data.map { it.toUi() }
                val picQuest = activitiesResult.data.firstOrNull { it.type == ActivityType.PICQUEST }
                updateState { state ->
                    state.copy(
                        activities = activityItems,
                        activityId = picQuest?.id.orEmpty(),
                        promptAudioUrl = picQuest?.payload?.audioUrl.orEmpty(),
                        options = picQuest?.payload?.options.orEmpty(),
                        isRefreshing = false,
                        isLoading = false,
                    )
                }
            } else if (activitiesResult is Resource.Error) {
                emitEvent(PicQuestEvent.ShowError(message = activitiesResult.message))
                updateState { state -> state.copy(isRefreshing = false, isLoading = false) }
            }
        }
    }

    private fun playPromptAudio() {
        viewModelScope.launch {
            val url = getCurrentState().promptAudioUrl
            if (url.isBlank()) {
                emitEvent(PicQuestEvent.ShowToast(message = "Audio topilmadi"))
                return@launch
            }
            emitEvent(PicQuestEvent.PlayAudio(url = url))
        }
    }

    private fun onOptionClicked(option: com.javohir.domain.model.ActivityOption) {
        viewModelScope.launch {
            val currentState = getCurrentState()
            val activityId = currentState.activityId
            if (activityId.isBlank()) {
                emitEvent(PicQuestEvent.ShowError(message = "Activity ID topilmadi"))
                return@launch
            }

            if (!option.isCorrect) {
                emitEvent(PicQuestEvent.ShowToast(message = "Noto'g'ri javob"))
                return@launch
            }

            val progressResult = progressUseCase(activityId = activityId).first { it !is Resource.Loading }
            if (progressResult is Resource.Error) {
                emitEvent(PicQuestEvent.ShowError(message = progressResult.message))
            } else {
                emitEvent(PicQuestEvent.ShowToast(message = "To'g'ri javob"))
                emitEvent(
                    PicQuestEvent.NavigateToTopic(
                        topicId = currentState.topicId,
                        subCategoryName = currentState.subCategoryName,
                    )
                )
            }
        }
    }

    private fun Activity.toUi(): PicQuestActivityItem {
        return PicQuestActivityItem(
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
