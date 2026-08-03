package com.javohir.data.model.response.profile

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.model.response.profile
 * Description: ProfileResponse: API JSON mos keladigan data klasslari.
 */

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("timestamp")
    val timestamp: String? = null,
    @SerializedName("data")
    val data: ProfileData? = null,
)
