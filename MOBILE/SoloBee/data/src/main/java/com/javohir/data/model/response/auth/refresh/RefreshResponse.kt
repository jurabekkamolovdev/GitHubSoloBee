package com.javohir.data.model.response.auth.refresh

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.model.response.auth.refresh
 * Description: RefreshResponse: API JSON mos keladigan data klasslari.
 */

import com.google.gson.annotations.SerializedName

data class RefreshResponse(
    @SerializedName(value = "status")
    val status: String,
    @SerializedName("timestamp")
    val timestamp: String,
    val data: RefreshData,
)
