package com.javohir.domain.useCase

import com.javohir.domain.model.SplashDestination
import com.javohir.domain.repository.SplashRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.domain.useCase
 * Description: Splash tugagandan keyingi yoʻnalishni aniqlash: access token boʻlmasa onboarding yoki welcome, boʻlsa uy ekrani.
 */
class ResolveSplashDestinationUseCase @Inject constructor(
    private val splashRepository: SplashRepository,
) {

    suspend operator fun invoke(): SplashDestination {
        if (splashRepository.hasAccessToken().first()) {
            return SplashDestination.Home
        }
        // Onboarding tugagan, lekin token yo'q — foydalanuvchi Login yoki Sign Up ni o'zi tanlaydi.
        // Bu yerda to'g'ridan-to'g'ri Login'ga yuborilsa, akkaunti yo'q foydalanuvchi
        // Register ekraniga boshqa hech qachon yetib bora olmaydi.
        return if (splashRepository.hasUserId().first()) {
            SplashDestination.Welcome
        } else {
            SplashDestination.OnBoarding
        }
    }
}
