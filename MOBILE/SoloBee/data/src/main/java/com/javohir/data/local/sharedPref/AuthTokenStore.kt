package com.javohir.data.local.sharedPref

interface AuthTokenStore {

    suspend fun getAccessToken(): String?

    suspend fun getRefreshToken(): String?

    suspend fun saveAccessToken(accessToken: String)

    suspend fun saveRefreshToken(refreshToken: String)

    suspend fun clearSession()
}
