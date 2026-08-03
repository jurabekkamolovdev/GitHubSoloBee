package com.javohir.data.model.response.avatars

import com.google.gson.annotations.SerializedName

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.model.response.avatars
 * Description: Response model
 */
data class AvatarsResponse(
    @SerializedName(value = "status")
    val status: String? = null,
    @SerializedName(value = "timestamp")
    val timestamp: String? = null,
    @SerializedName(value = "data")
    val `data`: AvatarsData? = null
)