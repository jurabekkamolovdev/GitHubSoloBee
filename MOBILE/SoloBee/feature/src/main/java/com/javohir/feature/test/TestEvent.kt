package com.javohir.feature.test

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.test
 * Description: Events
 */
sealed class TestEvent {

    data class ShowError(val message: String) : TestEvent()
    data class ShowToast(val message: String) : TestEvent()
    data class PlayAudio(val url: String) : TestEvent()
    data class NavigateToWordHunt(
        val topicId: String,
        val subCategoryName: String,
    ) : TestEvent()
}