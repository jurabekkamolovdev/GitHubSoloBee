package com.javohir.feature.category

import androidx.compose.ui.graphics.Color


/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.category
 * Description: UI State
 */
data class CategoryState(
    val firstName: String = "",
    val userInitial: String = "",
    val score: String = "",
    val isLoadingProfile: Boolean = false,
    val isRefreshing: Boolean = false,
    val name: String = "",
    val badgeText: String = "",
    val subCategories: List<SubCategoryItem> = emptyList(),
    val footerName: String = "",
    val footerLessonCount: String = "",
    val footerBackgroundColor: Color = Color(0xFF2196F3),
    val footerForegroundColor: Color = Color(0xFF03A9F4)
)

data class SubCategoryItem(
    val id: String,
    val categoryId: String,
    val image: String,
    val subName: String
)