package com.javohir.feature.learn

import com.javohir.feature.writing.WritingMode

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.learn
 * Description: UI State
 */
data class LearnState(
    val topicId: String = "",
    val subCategoryName: String = "",
    val learnActivityId: String = "",
    val learnImageUrl: String = "",
    val learnAudioUrl: String = "",
    val learnCompleted: Boolean = false,
    /** Topic'da WRITING activity bo'lmasa null. */
    val writingMode: WritingMode? = null,
    val score: String = "",
    val isRefreshing: Boolean = false,
    val isLoadingProfile: Boolean = false,
    val isLoadingActivity: Boolean = false,
    val activities: List<LearnActivityItem> = emptyList(),
)

data class LearnActivityItem(
    val id: String,
    val title: String,
    val enabled: Boolean,
    val completed: Boolean,
    val payloadImageUrl: String? = null,
)