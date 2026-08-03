package com.javohir.domain.repository

import kotlinx.coroutines.flow.Flow

interface SessionRepository {

    fun observeSessionExpired(): Flow<Unit>

    suspend fun clearSessionAndNotify()
}
