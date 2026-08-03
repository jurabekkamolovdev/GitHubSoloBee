package com.javohir.data.remote

import com.javohir.data.local.sharedPref.AuthTokenStore

class FakeAuthTokenStore : AuthTokenStore {

    var accessToken: String? = "expired_access"
    var refreshToken: String? = "stale_refresh"
    var isSessionCleared: Boolean = false

    override suspend fun getAccessToken(): String? = accessToken

    override suspend fun getRefreshToken(): String? = refreshToken

    override suspend fun saveAccessToken(accessToken: String) {
        this.accessToken = accessToken
    }

    override suspend fun saveRefreshToken(refreshToken: String) {
        this.refreshToken = refreshToken
    }

    override suspend fun clearSession() {
        accessToken = null
        refreshToken = null
        isSessionCleared = true
    }
}
