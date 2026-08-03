package com.javohir.data.model.request

import com.google.gson.annotations.SerializedName

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.model.request
 * Description: Request model
 */
data class RefreshRequest(
    @SerializedName(value = "refreshToken")
    val refreshToken: String,
)
