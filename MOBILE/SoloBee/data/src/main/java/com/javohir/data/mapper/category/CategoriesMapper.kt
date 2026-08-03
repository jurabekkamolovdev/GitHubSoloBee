package com.javohir.data.mapper.category

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.mapper.category
 * Description: CategoriesMapper: network modeldan domain mapping.
 */

import com.javohir.data.model.response.category.CategoriesResponse
import com.javohir.data.model.response.category.CategoryData
import com.javohir.data.model.response.category.DataSubCategory
import com.javohir.domain.model.Category
import com.javohir.domain.model.SubCategory

fun CategoriesResponse.toDomain(): List<Category> {
    return data.map { it.toDomain() }
}

fun CategoryData.toDomain(): Category {
    val mappedSubCategories = subCategories.map { it.toDomain() }

    val imagesFromSubCategories = mappedSubCategories
        .map { it.imageUrl }
        .filter { it.isNotBlank() }
        .take(n = 3)

    return Category(
        id = id,
        name = name,
        backgroundColor = backgroundColor,
        foregroundColor = foregroundColor,
        lessonCount = lessonCount,
        subCategory = mappedSubCategories,
        images = imagesFromSubCategories,
    )
}

fun DataSubCategory.toDomain(): SubCategory {
    return SubCategory(
        id = id,
        categoryId = categoryId,
        name = name,
        imageUrl = imageUrl,
    )
}
