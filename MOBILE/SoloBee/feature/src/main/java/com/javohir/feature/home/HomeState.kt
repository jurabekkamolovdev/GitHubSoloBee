package com.javohir.feature.home

import androidx.compose.ui.graphics.Color

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.home
 * Description: UI State
 */

data class HomeState(
    val firstName: String = "",
    val userInitial: String = "",
    val score: String = "",
    val isLoadingProfile: Boolean = false,
    val isLoadingCategories: Boolean = false,
    val isRefreshing: Boolean = false,
    val categories: List<CategoryItem> = emptyList()
)

data class CategoryItem(
    val id: String,
    val foregroundColor: Color,
    val backgroundColor: Color,
    val name: String,
    val images: List<String>,
    val lessonCount: String
)
