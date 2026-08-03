package com.javohir.feature.onBoarding
/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.onBoarding
 * Description: Event's
 */
sealed class OnBoardingEvent {
    data object NavigateToWelcome: OnBoardingEvent()
}