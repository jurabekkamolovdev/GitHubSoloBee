package com.javohir.feature.writing

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
 * Package: com.javohir.feature.writing
 * Description: ViewModel
 */
@HiltViewModel
class WritingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val loadProfileWithActivitiesUseCase: LoadProfileWithActivitiesUseCase,
    private val progressUseCase: ProgressUseCase,
) : BaseViewModel<WritingState, WritingEvent>(initialState = WritingState()) {

    private val navTopicId: String? = savedStateHandle[TOPIC_ID_ARG]
    private val navSubCategoryName: String? = savedStateHandle[SUB_CATEGORY_NAME_ARG]

    init {
        loadData()
    }

    fun onAction(intent: WritingIntent) {
        when (intent) {
            is WritingIntent.PlayCharAudio -> playCharAudio()
            is WritingIntent.TraceFinished -> traceFinished()
            is WritingIntent.Submit -> submit()
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            updateState { state ->
                state.copy(
                    topicId = navTopicId.orEmpty(),
                    subCategoryName = navSubCategoryName.orEmpty(),
                    isLoadingProfile = true,
                    isLoadingActivity = true,
                )
            }

            val topicId = navTopicId
            if (topicId.isNullOrBlank()) {
                emitEvent(WritingEvent.ShowError(message = "topicId topilmadi"))
                updateState { state ->
                    state.copy(isLoadingProfile = false, isLoadingActivity = false)
                }
                return@launch
            }

            val (profileResult, activitiesResult) = loadProfileWithActivitiesUseCase(
                topicId = topicId,
                forceRefresh = false,
            )

            when (profileResult) {
                is Resource.Success -> updateState { state ->
                    state.copy(score = profileResult.data.score, isLoadingProfile = false)
                }
                is Resource.Error -> {
                    emitEvent(WritingEvent.ShowError(message = profileResult.message))
                    updateState { state -> state.copy(isLoadingProfile = false) }
                }
                is Resource.Unauthorized -> updateState { state -> state.copy(isLoadingProfile = false) }
                is Resource.Loading -> Unit
            }

            when (activitiesResult) {
                is Resource.Success -> {
                    val activityItems = activitiesResult.data.map { it.toUi() }
                    val writingActivity = activitiesResult.data
                        .firstOrNull { activity -> activity.type == ActivityType.WRITING }
                    val char = writingActivity?.payload?.char.orEmpty()
                    updateState { state ->
                        state.copy(
                            activities = activityItems,
                            activityId = writingActivity?.id.orEmpty(),
                            char = char,
                            audioUrl = writingActivity?.payload?.audioUrl.orEmpty(),
                            completed = writingActivity?.completed == true,
                            steps = WritingState.stepsOf(char = char),
                            stepIndex = 0,
                            attempt = 0,
                            isLoadingActivity = false,
                        )
                    }
                    if (char.isBlank()) {
                        emitEvent(WritingEvent.ShowError(message = "Chiziladigan belgi topilmadi"))
                    }
                }
                is Resource.Error -> {
                    emitEvent(WritingEvent.ShowError(message = activitiesResult.message))
                    updateState { state -> state.copy(isLoadingActivity = false) }
                }
                is Resource.Unauthorized -> Unit
                is Resource.Loading -> Unit
            }
        }
    }

    /**
     * Joriy belgi chizib bo'lindi. Keyingi qadamga o'tamiz; oxirgisi bo'lsa
     * stepIndex chegaradan oshadi va [WritingState.allStepsDone] yonadi.
     */
    private fun traceFinished() {
        viewModelScope.launch {
            updateState { state ->
                if (state.allStepsDone) state else state.copy(stepIndex = state.stepIndex + 1)
            }
        }
    }

    private fun playCharAudio() {
        viewModelScope.launch {
            val audioUrl = getCurrentState().audioUrl
            if (audioUrl.isBlank()) {
                emitEvent(WritingEvent.ShowToast(message = "Audio topilmadi"))
                return@launch
            }
            emitEvent(WritingEvent.PlayAudio(url = audioUrl))
        }
    }

    private fun submit() {
        viewModelScope.launch {
            val currentState = getCurrentState()
            if (currentState.isSubmitting) return@launch

            val activityId = currentState.activityId
            if (activityId.isBlank()) {
                emitEvent(WritingEvent.ShowError(message = "Activity ID topilmadi"))
                return@launch
            }
            if (!currentState.allStepsDone) {
                emitEvent(WritingEvent.ShowToast(message = "Avval belgini chizing"))
                return@launch
            }

            updateState { state -> state.copy(isSubmitting = true) }
            val progressResult = progressUseCase(
                activityId = activityId,
                result = currentState.char,
            ).first { it !is Resource.Loading }
            updateState { state -> state.copy(isSubmitting = false) }

            when (progressResult) {
                is Resource.Success -> {
                    if (progressResult.data.isCompleted) {
                        emitEvent(WritingEvent.ShowToast(message = "To'g'ri javob"))
                        emitEvent(
                            WritingEvent.NavigateToWordHunt(
                                topicId = currentState.topicId,
                                subCategoryName = currentState.subCategoryName,
                            )
                        )
                    } else {
                        emitEvent(WritingEvent.ShowToast(message = "Qaytadan urinib ko'ring"))
                        updateState { state ->
                            state.copy(stepIndex = 0, attempt = state.attempt + 1)
                        }
                    }
                }
                is Resource.Error -> emitEvent(WritingEvent.ShowError(message = progressResult.message))
                is Resource.Unauthorized -> Unit
                is Resource.Loading -> Unit
            }
        }
    }

    private fun Activity.toUi(): WritingActivityItem {
        return WritingActivityItem(
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
