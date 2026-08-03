package com.javohir.data.model.response.category

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.model.response.category
 * Description: CategoriesResponse: API JSON mos keladigan data klasslari.
 */

import com.google.gson.annotations.SerializedName

data class CategoriesResponse(
    @SerializedName(value = "status")
    val status: String,
    @SerializedName(value = "timestamp")
    val timestamp: String,
    @SerializedName(value = "data")
    val data: List<CategoryData>,
)
