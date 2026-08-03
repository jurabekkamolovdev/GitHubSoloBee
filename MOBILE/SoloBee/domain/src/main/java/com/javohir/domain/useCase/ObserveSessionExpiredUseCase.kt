package com.javohir.domain.useCase

import com.javohir.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveSessionExpiredUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
) {

    operator fun invoke(): Flow<Unit> = sessionRepository.observeSessionExpired()
}
