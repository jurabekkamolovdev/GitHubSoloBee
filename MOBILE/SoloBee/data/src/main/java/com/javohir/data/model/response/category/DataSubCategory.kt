package com.javohir.data.model.response.category

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.model.response.category
 * Description: DataSubCategory: loyiha moduli uchun Kotlin manba.
 */

import com.google.gson.annotations.SerializedName

data class DataSubCategory(
    @SerializedName(value = "id")
    val id: String,
    @SerializedName(value = "categoryId")
    val categoryId: String,
    @SerializedName(value = "name")
    val name: String,
    @SerializedName(value = "thumbnailUrl")
    val imageUrl: String,
)
