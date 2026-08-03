package com.javohir.data.model.response.progress

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.model.response.progress
 * Description: ProgressResponse: API JSON mos keladigan data klasslari.
 */

import com.google.gson.annotations.SerializedName

data class ProgressResponse(
    @SerializedName(value = "status")
    val status: String,
    @SerializedName(value = "timestamp")
    val timestamp: String,
    @SerializedName(value = "data")
    val data: ProgressData,
)
