package com.javohir.data.model.response.auth.login

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.model.response.auth.login
 * Description: LoginResponse: API JSON mos keladigan data klasslari.
 */

data class LoginResponse(
    val status: String,
    val timestamp: String,
    val message: String,
    val data: LoginData,
)
