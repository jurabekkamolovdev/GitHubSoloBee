package com.javohir.data.mapper.activity

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.mapper.activity
 * Description: ActivitiesMapper: network modeldan domain mapping.
 */

import com.javohir.data.model.response.activity.ActivitiesResponse
import com.javohir.data.model.response.activity.ActivityData
import com.javohir.data.model.response.activity.ActivityOptionPayload
import com.javohir.data.model.response.activity.ActivityPayload
import com.javohir.domain.model.Activity
import com.javohir.domain.model.ActivityOption
import com.javohir.domain.model.ActivityPayload as DomainActivityPayload
import com.javohir.domain.model.ActivityType

fun ActivitiesResponse.toDomain(): List<Activity> = data.mapNotNull { activityData ->
    runCatching { activityData.toDomain() }.getOrNull()
}

fun ActivityData.toDomain(): Activity = Activity(
    id = runCatching { id }.getOrDefault(""),
    topicId = runCatching { topicId }.getOrDefault(""),
    type = runCatching { type.toActivityType() }.getOrDefault(ActivityType.UNKNOWN),
    title = runCatching { title }.getOrDefault(""),
    enabled = runCatching { enabled }.getOrDefault(false),
    completed = runCatching { completed }.getOrDefault(false),
    attemptCount = runCatching { attemptCount }.getOrDefault(0),
    totalAttemptCount = runCatching { totalAttemptCount }.getOrDefault(0),
    payload = runCatching { payload.toDomain() }.getOrDefault(
        DomainActivityPayload(
            mode = null,
            char = null,
            answer = null,
            imageUrl = null,
            audioUrl = null,
            options = null,
        )
    ),
)

fun ActivityPayload.toDomain(): DomainActivityPayload = DomainActivityPayload(
    mode = runCatching { mode }.getOrNull(),
    char = runCatching { char }.getOrNull(),
    answer = runCatching { answer }.getOrNull(),
    imageUrl = runCatching { imageUrl }.getOrNull(),
    audioUrl = runCatching { audioUrl }.getOrNull(),
    options = runCatching { options?.mapNotNull { runCatching { it.toDomain() }.getOrNull() } }
        .getOrNull(),
)

fun ActivityOptionPayload.toDomain(): ActivityOption = ActivityOption(
    char = runCatching { char }.getOrNull(),
    text = runCatching { text }.getOrNull(),
    isCorrect = runCatching { isCorrect }.getOrDefault(false),
    imageUrl = runCatching { imageUrl }.getOrDefault(""),
    audioUrl = runCatching { audioUrl }.getOrNull(),
)

private fun String.toActivityType(): ActivityType = when (uppercase()) {
    "LEARN" -> ActivityType.LEARN
    "WORDHUNT" -> ActivityType.WORDHUNT
    "PICQUEST" -> ActivityType.PICQUEST
    "WRITING" -> ActivityType.WRITING
    else -> ActivityType.UNKNOWN
}
