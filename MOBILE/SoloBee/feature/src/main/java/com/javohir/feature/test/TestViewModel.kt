package com.javohir.feature.test

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
 * Package: com.javohir.feature.test
 * Description: ViewModel
 */
@HiltViewModel
class TestViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val loadProfileWithActivitiesUseCase: LoadProfileWithActivitiesUseCase,
    private val progressUseCase: ProgressUseCase,
) : BaseViewModel<TestState, TestEvent>(initialState = TestState()) {

    private val navTopicId: String? = savedStateHandle[TOPIC_ID_ARG]
    private val navSubCategoryName: String? = savedStateHandle[SUB_CATEGORY_NAME_ARG]

    init {
        loadData()
    }

    fun onAction(intent: TestIntent) {
        when (intent) {
            is TestIntent.Refresh -> refreshData()
            is TestIntent.PlayWordAudio -> playWordAudio()
            is TestIntent.SelectLetter -> selectLetter(index = intent.index)
            is TestIntent.RemoveLetter -> removeLetter(typedPosition = intent.typedPosition)
            is TestIntent.Clear -> clearTyped()
            is TestIntent.Submit -> submit()
        }
    }

    private fun loadData() {
        refreshInternal(showPullRefresh = false)
    }

    private fun refreshData() {
        refreshInternal(showPullRefresh = true)
    }

    private fun refreshInternal(showPullRefresh: Boolean) {
        viewModelScope.launch {
            updateState { state ->
                state.copy(
                    isRefreshing = showPullRefresh,
                    topicId = navTopicId.orEmpty(),
                    subCategoryName = navSubCategoryName.orEmpty(),
                    isLoadingProfile = true,
                    isLoadingActivity = true,
                )
            }

            val topicId = navTopicId
            if (topicId.isNullOrBlank()) {
                emitEvent(TestEvent.ShowError(message = "topicId topilmadi"))
                updateState { state ->
                    state.copy(
                        isRefreshing = false,
                        isLoadingProfile = false,
                        isLoadingActivity = false,
                    )
                }
                return@launch
            }

            val (profileResult, activitiesResult) = loadProfileWithActivitiesUseCase(
                topicId = topicId,
                forceRefresh = showPullRefresh,
            )

            when (profileResult) {
                is Resource.Success -> updateState { state ->
                    state.copy(score = profileResult.data.score, isLoadingProfile = false)
                }
                is Resource.Error -> {
                    emitEvent(TestEvent.ShowError(message = profileResult.message))
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
                    val letterOptions = writingActivity?.payload?.options
                        ?.mapIndexedNotNull { index, option ->
                            val char = option.char?.takeIf { it.isNotBlank() } ?: return@mapIndexedNotNull null
                            TestLetterOption(
                                index = index,
                                char = char.uppercase(),
                                imageUrl = option.imageUrl,
                            )
                        }
                        .orEmpty()
                    updateState { state ->
                        state.copy(
                            activities = activityItems,
                            activityId = writingActivity?.id.orEmpty(),
                            answer = writingActivity?.payload?.answer.orEmpty(),
                            imageUrl = writingActivity?.payload?.imageUrl.orEmpty(),
                            audioUrl = writingActivity?.payload?.audioUrl.orEmpty(),
                            completed = writingActivity?.completed == true,
                            options = letterOptions,
                            typed = emptyList(),
                            isLoadingActivity = false,
                        )
                    }
                }
                is Resource.Error -> {
                    emitEvent(TestEvent.ShowError(message = activitiesResult.message))
                    updateState { state -> state.copy(isLoadingActivity = false) }
                }
                is Resource.Unauthorized -> Unit
                is Resource.Loading -> Unit
            }

            updateState { state -> state.copy(isRefreshing = false) }
        }
    }

    private fun selectLetter(index: Int) {
        viewModelScope.launch {
            updateState { state ->
                val option = state.options.getOrNull(index) ?: return@updateState state
                if (option.used) return@updateState state
                if (state.answer.isNotBlank() && state.typed.size >= state.answer.length) {
                    return@updateState state
                }
                state.copy(
                    options = state.options.map { if (it.index == index) it.copy(used = true) else it },
                    typed = state.typed + TypedLetter(optionIndex = index, char = option.char),
                )
            }
        }
    }

    private fun removeLetter(typedPosition: Int) {
        viewModelScope.launch {
            updateState { state ->
                val removed = state.typed.getOrNull(typedPosition) ?: return@updateState state
                state.copy(
                    options = state.options.map {
                        if (it.index == removed.optionIndex) it.copy(used = false) else it
                    },
                    typed = state.typed.filterIndexed { i, _ -> i != typedPosition },
                )
            }
        }
    }

    private fun clearTyped() {
        viewModelScope.launch {
            updateState { state ->
                state.copy(
                    options = state.options.map { it.copy(used = false) },
                    typed = emptyList(),
                )
            }
        }
    }

    private fun playWordAudio() {
        viewModelScope.launch {
            val audioUrl = getCurrentState().audioUrl
            if (audioUrl.isBlank()) {
                emitEvent(TestEvent.ShowToast(message = "Audio topilmadi"))
                return@launch
            }
            emitEvent(TestEvent.PlayAudio(url = audioUrl))
        }
    }

    private fun submit() {
        viewModelScope.launch {
            val currentState = getCurrentState()
            if (currentState.isSubmitting) return@launch

            val activityId = currentState.activityId
            if (activityId.isBlank()) {
                emitEvent(TestEvent.ShowError(message = "Activity ID topilmadi"))
                return@launch
            }
            if (!currentState.isFullyTyped) {
                emitEvent(TestEvent.ShowToast(message = "Barcha harflarni tering"))
                return@launch
            }

            updateState { state -> state.copy(isSubmitting = true) }
            val progressResult = progressUseCase(
                activityId = activityId,
                result = currentState.typedText,
            ).first { it !is Resource.Loading }
            updateState { state -> state.copy(isSubmitting = false) }

            when (progressResult) {
                is Resource.Success -> {
                    // To'g'rilikni server tekshiradi: completed bo'lsa javob to'g'ri.
                    if (progressResult.data.isCompleted) {
                        emitEvent(TestEvent.ShowToast(message = "To'g'ri javob"))
                        emitEvent(
                            TestEvent.NavigateToWordHunt(
                                topicId = currentState.topicId,
                                subCategoryName = currentState.subCategoryName,
                            )
                        )
                    } else {
                        emitEvent(TestEvent.ShowToast(message = "Noto'g'ri javob"))
                        clearTyped()
                    }
                }
                is Resource.Error -> emitEvent(TestEvent.ShowError(message = progressResult.message))
                is Resource.Unauthorized -> Unit
                is Resource.Loading -> Unit
            }
        }
    }

    private fun Activity.toUi(): TestActivityItem {
        return TestActivityItem(
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