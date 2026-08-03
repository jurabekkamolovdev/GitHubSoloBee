package com.javohir.feature.onBoarding
/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.onBoarding
 * Description: User Action
 */
sealed class OnBoardingIntent {
    data object NextClicked: OnBoardingIntent()
    data object SkipClicked: OnBoardingIntent()
    data class PageChanged(val page: Int): OnBoardingIntent()
}