package com.javohir.feature.register

import androidx.annotation.StringRes

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.register
 * Description: Event's
 */
sealed class RegisterEvent {
    data object NavigateToLogin: RegisterEvent()
    data object NavigateBack: RegisterEvent()
    data class ShowErrorText(val message: String): RegisterEvent()
    data class ShowErrorRes(@get:StringRes val messageId: Int): RegisterEvent()
}