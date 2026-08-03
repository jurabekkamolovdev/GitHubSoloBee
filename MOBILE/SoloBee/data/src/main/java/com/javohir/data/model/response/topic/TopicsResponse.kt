package com.javohir.data.model.response.topic

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.model.response.topic
 * Description: TopicsResponse: API JSON mos keladigan data klasslari.
 */

import com.google.gson.annotations.SerializedName

data class TopicsResponse(
    @SerializedName(value = "status")
    val status: String,
    @SerializedName(value = "timestamp")
    val timestamp: String,
    val data: List<TopicData>,
)
