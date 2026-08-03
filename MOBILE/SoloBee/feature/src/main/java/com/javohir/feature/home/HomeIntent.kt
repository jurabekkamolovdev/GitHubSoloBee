package com.javohir.feature.home

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.home
 * Description: User Action
 */
sealed class HomeIntent {
    data object Refresh: HomeIntent()
    data class CategoryClicked(val categoryId: String): HomeIntent()

    data object NavigateToProfile: HomeIntent()
}