package com.javohir.feature.wordHunt

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.wordhunt
 * Description: WordHuntEvent: navigatsiya va snackbar kabi bir martalik hodisalar.
 */

sealed class WordHuntEvent {
    data class ShowError(val message: String) : WordHuntEvent()
    data class ShowToast(val message: String) : WordHuntEvent()
    data class PlayAudio(val url: String) : WordHuntEvent()
    data class NavigateToPicQuest(
        val topicId: String,
        val subCategoryName: String,
    ) : WordHuntEvent()
}
