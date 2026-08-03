package com.javohir.data.model.response.activity

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.model.response.activity
 * Description: ActivityData: API JSON mos keladigan data klasslari.
 */

import com.google.gson.annotations.SerializedName

data class ActivityData(
    @SerializedName(value = "id")
    val id: String,
    @SerializedName(value = "topicId")
    val topicId: String,
    @SerializedName(value = "type")
    val type: String,
    @SerializedName(value = "title")
    val title: String,
    @SerializedName(value = "enabled")
    val enabled: Boolean,
    @SerializedName(value = "completed")
    val completed: Boolean,
    @SerializedName(value = "attemptCount")
    val attemptCount: Int,
    @SerializedName(value = "totalAttemptCount")
    val totalAttemptCount: Int,
    @SerializedName(value = "payload")
    val payload: ActivityPayload,
)
