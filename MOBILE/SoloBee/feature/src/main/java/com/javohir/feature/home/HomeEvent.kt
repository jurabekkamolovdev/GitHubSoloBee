package com.javohir.feature.home

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.home
 * Description: User Action's
 */
sealed class HomeEvent {

    data class ShowError(val message: String): HomeEvent()

    data class NavigateToCategories(val categoryId: String): HomeEvent()

    data object NavigateToProfile: HomeEvent()
}