package com.javohir.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.domain.repository
 * Description: Repository Interface
 */
interface SplashRepository {

    suspend fun hasAccessToken(): Flow<Boolean>

    suspend fun hasUserId(): Flow<Boolean>

    suspend fun markOnboardingCompleted()

}