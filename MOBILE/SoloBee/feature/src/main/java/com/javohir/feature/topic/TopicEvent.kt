package com.javohir.feature.topic

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.topic
 * Description: Events
 */
sealed class TopicEvent {

    data class ShowError(val message: String) : TopicEvent()

    data class NavigateToLearn(
        val topicId: String,
        val subCategoryName: String,
    ) : TopicEvent()
}