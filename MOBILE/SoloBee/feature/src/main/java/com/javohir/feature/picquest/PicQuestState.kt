package com.javohir.feature.picquest

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.picquest
 * Description: PicQuestState: MutableStateFlow / UI holat strukturasi.
 */

import com.javohir.domain.model.ActivityOption

data class PicQuestState(
    val topicId: String = "",
    val subCategoryName: String = "",
    val score: String = "",
    val activityId: String = "",
    val promptAudioUrl: String = "",
    val options: List<ActivityOption> = emptyList(),
    val activities: List<PicQuestActivityItem> = emptyList(),
    val isRefreshing: Boolean = false,
    val isLoading: Boolean = false,
)

data class PicQuestActivityItem(
    val id: String,
    val title: String,
    val enabled: Boolean,
    val completed: Boolean,
)
