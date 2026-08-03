package com.javohir.data.model.response.topic

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.model.response.topic
 * Description: TopicData: API JSON mos keladigan data klasslari.
 */

import com.google.gson.annotations.SerializedName

data class TopicData(
    @SerializedName(value = "id")
    val id: String,
    @SerializedName(value = "subCategoryId")
    val subCategoryId: String,
    @SerializedName(value = "thumbnailUrl")
    val imageUrl: String,
    @SerializedName(value = "enabled")
    val enabled: Boolean,
    @SerializedName(value = "completed")
    val completed: Boolean,
)
