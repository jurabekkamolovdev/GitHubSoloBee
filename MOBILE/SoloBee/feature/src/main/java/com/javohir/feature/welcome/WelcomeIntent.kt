package com.javohir.feature.welcome

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.welcome
 * Description: User Action
 */
sealed class WelcomeIntent {
    data object LoginClicked: WelcomeIntent()
    data object SignUpClicked: WelcomeIntent()
}