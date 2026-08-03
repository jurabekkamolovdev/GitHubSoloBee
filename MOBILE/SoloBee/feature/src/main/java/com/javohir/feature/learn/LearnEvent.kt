package com.javohir.feature.learn

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.learn
 * Description: Events
 */
sealed class LearnEvent {

    data class ShowError(val message: String): LearnEvent()
    data class ShowToast(val message: String): LearnEvent()
    data class NavigateToWriting(
        val topicId: String,
        val subCategoryName: String,
    ) : LearnEvent()
    data class NavigateToTrace(
        val topicId: String,
        val subCategoryName: String,
    ) : LearnEvent()
    data class NavigateToWordHunt(
        val topicId: String,
        val subCategoryName: String,
    ) : LearnEvent()
    data class PlayAudio(
        val url: String,
        val activityId: String,
    ): LearnEvent()
}