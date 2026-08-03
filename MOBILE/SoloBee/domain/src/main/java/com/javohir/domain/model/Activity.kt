package com.javohir.domain.model

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.domain.model
 * Description: model class
 */

data class Activity(
    val id: String,
    val topicId: String,
    val type: ActivityType,
    val title: String,
    val enabled: Boolean,
    val completed: Boolean,
    val attemptCount: Int,
    val totalAttemptCount: Int,
    val payload: ActivityPayload,
)

data class ActivityPayload(
    val mode: String?,
    val char: String?,
    val answer: String?,
    val imageUrl: String?,
    val audioUrl: String?,
    val options: List<ActivityOption>?,
)

data class ActivityOption(
    val char: String?,
    val text: String?,
    val isCorrect: Boolean,
    val imageUrl: String,
    val audioUrl: String?,
)

enum class ActivityType {
    LEARN,
    WORDHUNT,
    PICQUEST,
    WRITING,
    UNKNOWN
}
