package com.javohir.feature.writing

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.writing
 * Description: Events
 */
sealed class WritingEvent {

    data class ShowError(val message: String) : WritingEvent()
    data class ShowToast(val message: String) : WritingEvent()
    data class PlayAudio(val url: String) : WritingEvent()
    data class NavigateToWordHunt(
        val topicId: String,
        val subCategoryName: String,
    ) : WritingEvent()
}
