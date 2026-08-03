package com.javohir.feature.topic

import com.javohir.feature.R

/**
 * Created by: Javohir Oromov macOS
 * Project: SoloBee
 * Package: com.javohir.feature.topic
 * Description: UI State
 */
data class TopicState(
    val subCategoryId: String = "",
    val subCategoryName: String = "",
    val isLoadingProfile: Boolean = false,
    val isLoadingTopics: Boolean = false,
    val score: String = "",
    val topics: List<TopicItem> = emptyList()
) {
    val isLoading: Boolean get() = isLoadingProfile || isLoadingTopics
}

data class TopicItem(
    val id: String,
    val subCategoryId: String,
    val imageUrl: String,
    val enabled: Boolean,
    val completed: Boolean
)

fun TopicItem.tileBackgroundResId(): Int = when {
    completed -> R.drawable.check
    enabled -> R.drawable.enabled
    else -> R.drawable.disabled
}