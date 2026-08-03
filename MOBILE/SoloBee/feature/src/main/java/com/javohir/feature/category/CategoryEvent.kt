package com.javohir.feature.category

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.category
 * Description: Events
 */
sealed class CategoryEvent {

    data class ShowError(val message: String): CategoryEvent()
    data class ShowToast(val message: String): CategoryEvent()

    data class NavigateToTopics(
        val subCategoryId: String,
        val subCategoryName: String,
    ) : CategoryEvent()

    data object NavigateToProfile: CategoryEvent()
}