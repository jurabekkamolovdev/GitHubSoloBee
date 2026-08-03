package com.javohir.feature.welcome

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.welcome
 * Description: Event's
 */
sealed class WelcomeEvent {
    data object NavigateToLogin: WelcomeEvent()
    data object NavigateToRegister: WelcomeEvent()
}