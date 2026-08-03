package com.javohir.domain.useCase

import com.javohir.domain.repository.SplashRepository
import javax.inject.Inject

class MarkOnboardingCompletedUseCase @Inject constructor(
    private val splashRepository: SplashRepository,
) {

    suspend operator fun invoke() {
        splashRepository.markOnboardingCompleted()
    }
}
