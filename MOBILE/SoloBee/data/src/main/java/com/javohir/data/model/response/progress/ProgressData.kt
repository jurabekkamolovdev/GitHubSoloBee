package com.javohir.data.model.response.progress

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.model.response.progress
 * Description: ProgressData: API JSON mos keladigan data klasslari.
 */

import com.google.gson.annotations.SerializedName

data class ProgressData(
    @SerializedName(value = "attemptCount", alternate = ["attempt_count"])
    val attemptCount: Int,
    @SerializedName(value = "threshold")
    val threshold: Int,
    @SerializedName(value = "completed")
    val completed: String,
)
