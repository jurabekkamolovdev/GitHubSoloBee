package com.javohir.feature.picquest

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.picquest
 * Description: PicQuestEvent: navigatsiya va snackbar kabi bir martalik hodisalar.
 */

sealed class PicQuestEvent {
    data class ShowError(val message: String) : PicQuestEvent()
    data class ShowToast(val message: String) : PicQuestEvent()
    data class PlayAudio(val url: String) : PicQuestEvent()
    data class NavigateToTopic(val topicId: String, val subCategoryName: String) : PicQuestEvent()
}
