package com.javohir.data.model.response.activity

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.model.response.activity
 * Description: ActivitiesResponse: API JSON mos keladigan data klasslari.
 */

import com.google.gson.annotations.SerializedName

data class ActivitiesResponse(
    @SerializedName(value = "status")
    val status: String,
    @SerializedName(value = "timestamp")
    val timestamp: String,
    @SerializedName(value = "data")
    val data: List<ActivityData>,
)
