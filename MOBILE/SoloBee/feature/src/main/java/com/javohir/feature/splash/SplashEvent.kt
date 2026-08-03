package com.javohir.feature.splash

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.splash
 * Description: Events
 */
sealed class SplashEvent {

    object NavigateToHome: SplashEvent()
    object NavigateToWelcome: SplashEvent()
    object NavigateToOnBoarding: SplashEvent()

}