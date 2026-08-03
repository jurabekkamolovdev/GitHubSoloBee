package com.javohir.domain.model

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.domain.model
 * Description: model class
 */
data class Topic(
    val id: String,
    val subCategoryId: String,
    val imageUrl: String,
    val enabled: Boolean,
    val completed: Boolean,
)