package com.javohir.feature.category


/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.category
 * Description: User Action
 */
sealed class CategoryIntent {

    data object Refresh: CategoryIntent()

    data class OpenTopics(
        val subCategoryId: String,
        val subCategoryName: String,
    ) : CategoryIntent()

    data object NavigateToProfile: CategoryIntent()
}