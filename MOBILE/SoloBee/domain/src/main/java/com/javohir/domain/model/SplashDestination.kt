package com.javohir.domain.model

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.domain.model
 * Description: SplashDestination: splashdan keyingi marshrut (sealed).
 */

/**
 * Splash tugaguncha qayerga oʻtish — application kirish ssenariysi natijasi.
 */

sealed class SplashDestination {

    data object Home : SplashDestination()

    data object Welcome : SplashDestination()

    data object OnBoarding : SplashDestination()
}
