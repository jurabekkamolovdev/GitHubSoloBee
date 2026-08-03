package com.javohir.feature.topic

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.topic
 * Description: User Action
 */
sealed class TopicIntent {

    data class TopicClicked(val topicId: String) : TopicIntent()

    data object ScreenResumed : TopicIntent()
}