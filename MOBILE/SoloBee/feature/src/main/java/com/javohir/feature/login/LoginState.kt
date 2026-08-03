package com.javohir.feature.login
/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.login
 * Description: UI State
 */
data class LoginState(
    val usernameText: String = "",
    val passwordText: String = "",
    val isLoading: Boolean = false,
    )
