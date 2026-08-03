package com.javohir.domain.model

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.domain.model
 * Description: model class 
 */
data class Category(
    val id: String,
    val foregroundColor: String,
    val backgroundColor: String,
    val name: String,
    val lessonCount: Int,
    val subCategory: List<SubCategory>,
    val images: List<String>
)
