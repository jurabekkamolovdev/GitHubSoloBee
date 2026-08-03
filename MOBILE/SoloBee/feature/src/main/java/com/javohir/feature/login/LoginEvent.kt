package com.javohir.feature.login

import androidx.annotation.StringRes

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.login
 * Description: Event's
 */
sealed class LoginEvent {
    data object NavigateToHome: LoginEvent()
    data class ShowErrorText(val message: String): LoginEvent()
    data class ShowErrorRes(@get:StringRes val messageId: Int): LoginEvent()
}