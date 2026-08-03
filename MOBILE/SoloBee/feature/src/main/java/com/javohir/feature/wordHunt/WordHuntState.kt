package com.javohir.feature.wordHunt

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.wordhunt
 * Description: WordHuntState: MutableStateFlow / UI holat strukturasi.
 */

import com.javohir.domain.model.ActivityOption

data class WordHuntState(
    val topicId: String = "",
    val subCategoryName: String = "",
    val score: String = "",
    val activityId: String = "",
    val imageUrl: String = "",
    val activities: List<WordHuntActivityItem> = emptyList(),
    val options: List<ActivityOption> = emptyList(),
    val isRefreshing: Boolean = false,
    val isLoading: Boolean = false,
)

data class WordHuntActivityItem(
    val id: String,
    val title: String,
    val enabled: Boolean,
    val completed: Boolean,
)
