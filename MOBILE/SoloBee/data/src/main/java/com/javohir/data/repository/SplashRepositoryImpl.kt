package com.javohir.data.repository

import com.javohir.data.local.sharedPref.SharedPreference
import com.javohir.domain.repository.SplashRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.repository
 * Description: Repository Implementation
 */
class SplashRepositoryImpl@Inject constructor(
    private val preference: SharedPreference
): SplashRepository {
    override suspend fun hasAccessToken(): Flow<Boolean> {
        return flow {
            val token = preference.getAccessToken()
            emit(!token.isNullOrBlank())
        }
    }

    override suspend fun hasUserId(): Flow<Boolean> {
        return flow {
            emit(preference.hasCompletedOnboarding())
        }
    }

    override suspend fun markOnboardingCompleted() {
        preference.markOnboardingCompleted()
    }
}