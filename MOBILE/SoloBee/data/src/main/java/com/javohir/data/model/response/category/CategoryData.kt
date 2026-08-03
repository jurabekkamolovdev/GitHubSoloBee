package com.javohir.data.model.response.category

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.model.response.category
 * Description: CategoryData: API JSON mos keladigan data klasslari.
 */

import com.google.gson.annotations.SerializedName

data class CategoryData(
    @SerializedName(value = "id")
    val id: String,
    @SerializedName(value = "name")
    val name: String,
    @SerializedName(value = "backgroundColor")
    val backgroundColor: String,
    @SerializedName(value = "foregroundColor")
    val foregroundColor: String,
    @SerializedName(value = "lessonCount")
    val lessonCount: Int,
    @SerializedName(value = "subCategories")
    val subCategories: List<DataSubCategory>,
)
