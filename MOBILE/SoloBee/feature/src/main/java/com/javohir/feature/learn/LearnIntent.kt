package com.javohir.feature.learn

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.learn
 * Description: User Actions
 */

sealed class LearnIntent {

    object Refresh: LearnIntent()
    object PlayLearnAudio: LearnIntent()
    object OpenNext: LearnIntent()
    data class AudioPlayed(val activityId: String): LearnIntent()
}