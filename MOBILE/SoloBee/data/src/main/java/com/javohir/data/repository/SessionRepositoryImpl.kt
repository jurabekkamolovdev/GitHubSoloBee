package com.javohir.data.repository

import com.javohir.data.local.sharedPref.AuthTokenStore
import com.javohir.data.session.SessionExpiredNotifier
import com.javohir.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionRepositoryImpl @Inject constructor(
    private val sessionExpiredNotifier: SessionExpiredNotifier,
    private val authTokenStore: AuthTokenStore,
) : SessionRepository {

    override fun observeSessionExpired(): Flow<Unit> = sessionExpiredNotifier.events

    override suspend fun clearSessionAndNotify() {
        authTokenStore.clearSession()
        sessionExpiredNotifier.notifySessionExpired()
    }
}
