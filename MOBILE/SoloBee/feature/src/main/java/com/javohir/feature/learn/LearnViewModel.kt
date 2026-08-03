package com.javohir.feature.learn
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.javohir.domain.common.Resource
import com.javohir.domain.model.Activity
import com.javohir.domain.model.ActivityType
import com.javohir.domain.useCase.LoadProfileWithActivitiesUseCase
import com.javohir.domain.useCase.ProgressUseCase
import com.javohir.feature.writing.WritingMode
import com.javohir.utils.mvi.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.learn
 * Description: ViewModel
 */
@HiltViewModel
class LearnViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val loadProfileWithActivitiesUseCase: LoadProfileWithActivitiesUseCase,
    private val progressUseCase: ProgressUseCase,
): BaseViewModel<LearnState, LearnEvent>(initialState = LearnState()) {

    private val navTopicId: String? = savedStateHandle[TOPIC_ID_ARG]
    private val navSubCategoryName: String? = savedStateHandle[SUB_CATEGORY_NAME_ARG]

    init {
        loadData()
    }
    fun onAction(intent: LearnIntent){
        when (intent) {
            is LearnIntent.Refresh -> refreshData()
            is LearnIntent.PlayLearnAudio -> playLearnAudio()
            is LearnIntent.OpenNext -> openNext()
            is LearnIntent.AudioPlayed -> sendProgressAndReload(activityId = intent.activityId)
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
                    topicId = navTopicId.orEmpty(),
                    subCategoryName = navSubCategoryName.orEmpty(),
                    isLoadingProfile = true,
                    isLoadingActivity = true
                )
            }

            val topicId = navTopicId
            if (topicId.isNullOrBlank()) {
                emitEvent(LearnEvent.ShowError(message = "topicId topilmadi"))
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
                is Resource.Success -> {
                    updateState { state ->
                        state.copy(
                            score = profileResult.data.score,
                            isLoadingProfile = false,
                        )
                    }
                }
                is Resource.Error -> {
                    emitEvent(LearnEvent.ShowError(message = profileResult.message))
                    updateState { state -> state.copy(isLoadingProfile = false) }
                }
                is Resource.Unauthorized -> {
                    updateState { state -> state.copy(isLoadingProfile = false) }
                }
                is Resource.Loading -> Unit
            }

            when (activitiesResult) {
                is Resource.Success -> {
                    val activityItems = activitiesResult.data.map { it.toUi() }
                    val learnActivity = activitiesResult.data
                        .firstOrNull { activity -> activity.type == ActivityType.LEARN }
                    val writingActivity = activitiesResult.data
                        .firstOrNull { activity -> activity.type == ActivityType.WRITING }
                    updateState { state ->
                        state.copy(
                            activities = activityItems,
                            learnActivityId = learnActivity?.id.orEmpty(),
                            learnImageUrl = learnActivity?.payload?.imageUrl.orEmpty(),
                            learnAudioUrl = learnActivity?.payload?.audioUrl.orEmpty(),
                            learnCompleted = learnActivity?.completed == true,
                            // Mode tanilmasa spell'ga tushamiz — trace qo'shilgunga qadar
                            // barcha WRITING activity'lar spell edi.
                            writingMode = writingActivity?.let { activity ->
                                WritingMode.from(raw = activity.payload.mode) ?: WritingMode.SPELL
                            },
                            isLoadingActivity = false,
                        )
                    }
                }
                is Resource.Error -> {
                    emitEvent(LearnEvent.ShowError(message = activitiesResult.message))
                    updateState { state -> state.copy(isLoadingActivity = false) }
                }
                is Resource.Unauthorized -> Unit
                is Resource.Loading -> Unit
            }

            updateState { state -> state.copy(isRefreshing = false) }
        }
    }

    private fun Activity.toUi(): LearnActivityItem {
        return LearnActivityItem(
            id = id,
            title = title,
            enabled = enabled,
            completed = completed,
            payloadImageUrl = payload.imageUrl,
        )
    }

    private fun playLearnAudio() {
        viewModelScope.launch {
            val currentState = getCurrentState()
            val audioUrl = currentState.learnAudioUrl
            val activityId = currentState.learnActivityId
            if (audioUrl.isBlank()) {
                emitEvent(LearnEvent.ShowError(message = "Audio topilmadi"))
                return@launch
            }
            if (activityId.isBlank()) {
                emitEvent(LearnEvent.ShowError(message = "Activity ID topilmadi"))
                return@launch
            }
            emitEvent(
                LearnEvent.PlayAudio(
                    url = audioUrl,
                    activityId = activityId,
                )
            )
        }
    }

    private fun sendProgressAndReload(activityId: String) {
        viewModelScope.launch {
            val progressResult = progressUseCase(activityId = activityId).first { it !is Resource.Loading }
            if (progressResult is Resource.Error) {
                emitEvent(LearnEvent.ShowError(message = progressResult.message))
            }
            loadData()
        }
    }

    private fun openNext() {
        viewModelScope.launch {
            val currentState = getCurrentState()
            if (!currentState.learnCompleted) {
                emitEvent(LearnEvent.ShowToast(message = "Avval Learn ni tugating"))
                return@launch
            }
            // Response'da Writing bo'lsa: Learn -> Writing -> WordHunt.
            // Bo'lmasa: Learn -> WordHunt.
            val topicId = currentState.topicId
            val subCategoryName = currentState.subCategoryName
            val event = when (currentState.writingMode) {
                WritingMode.SPELL -> LearnEvent.NavigateToWriting(topicId, subCategoryName)
                WritingMode.TRACE -> LearnEvent.NavigateToTrace(topicId, subCategoryName)
                null -> LearnEvent.NavigateToWordHunt(topicId, subCategoryName)
            }
            emitEvent(event)
        }
    }

    private companion object {
        const val TOPIC_ID_ARG = "topicId"
        const val SUB_CATEGORY_NAME_ARG = "subCategoryName"
    }
}