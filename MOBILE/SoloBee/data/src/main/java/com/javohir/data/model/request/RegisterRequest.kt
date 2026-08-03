package com.javohir.data.model.request

import com.google.gson.annotations.SerializedName

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.model.request
 * Description: Request model
 */
data class RegisterRequest(
    @SerializedName(value = "firstName")
    val firstName: String,
    @SerializedName(value = "lastName")
    val lastName: String,
    @SerializedName(value = "userName")
    val userName: String,
    @SerializedName(value = "password")
    val password: String,
    @SerializedName(value = "age")
    val age: Int,
    @SerializedName(value = "avatarId")
    val avatarId: String
)
