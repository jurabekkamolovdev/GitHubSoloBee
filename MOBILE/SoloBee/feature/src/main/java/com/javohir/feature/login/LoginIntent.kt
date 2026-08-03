package com.javohir.feature.login

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.login
 * Description: User Action
 */
sealed class LoginIntent {
    data class UserNameChanged(val value: String): LoginIntent()
    data class PasswordChanged(val value: String): LoginIntent()
    data object LoginClicked: LoginIntent()
}