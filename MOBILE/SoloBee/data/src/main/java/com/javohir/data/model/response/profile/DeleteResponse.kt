package com.javohir.data.model.response.profile

import com.google.gson.annotations.SerializedName

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.model.response.profile
 * Description: Delete ApI Response
 */
data class DeleteResponse(
    @SerializedName(value = "status")
    val status: String,
    @SerializedName(value = "timestamp")
    val timestamp: String,
    @SerializedName(value = "data")
    val data: Boolean,
)
