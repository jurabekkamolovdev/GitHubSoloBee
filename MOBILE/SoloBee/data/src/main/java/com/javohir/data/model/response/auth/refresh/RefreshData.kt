package com.javohir.data.model.response.auth.refresh

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.model.response.auth.refresh
 * Description: RefreshData: API JSON mos keladigan data klasslari.
 */

import com.google.gson.annotations.SerializedName

data class RefreshData(
    @SerializedName(value = "access_token", alternate = ["accessToken"])
    val accessToken: String,
    @SerializedName(value = "refresh_token", alternate = ["refreshToken"])
    val refreshToken: String,
)
