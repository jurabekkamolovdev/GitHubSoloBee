package com.javohir.data.model.request
import com.google.gson.annotations.SerializedName

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.model.request
 * Description: Request model
 */
data class LoginRequest(
    @SerializedName(value = "username")
    val userName: String,
    @SerializedName(value = "password")
    val password: String
)
