package com.javohir.data.remote

import com.javohir.data.local.sharedPref.AuthTokenStore
import com.javohir.data.model.request.RefreshRequest
import com.javohir.data.session.SessionExpiredNotifier
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by: Javohir Oromov macOS
 * Project: SoloBee
 * Package: com.javohir.data.remote
 * Description: We will renew the Access Token.
 */
@Singleton
class TokenAuthenticator @Inject constructor(
    private val preference: AuthTokenStore,
    private val refreshApiService: RefreshApiService,
    private val sessionExpiredNotifier: SessionExpiredNotifier,
): Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 2) return null
        if (response.request.header("Authorization").isNullOrBlank()) return null


        synchronized(refreshMutex) {
            val failedAccess = response.request.bearerTokenOrNull()
            val latestAccess = runBlocking { preference.getAccessToken() }
            if (!latestAccess.isNullOrBlank() && latestAccess != failedAccess) {
                return response.request.withBearerToken(latestAccess)
            }

            val refreshToken = runBlocking {
                preference.getRefreshToken()
            }?.trim()?.takeIf { it.isNotBlank() } ?: return null

            val refreshResponse = try {
                runBlocking {
                    refreshApiService.refreshToken(
                        RefreshRequest(refreshToken = refreshToken)
                    )
                }
            } catch (_: Exception) {
                null
            } ?: return null

            if (!refreshResponse.isSuccessful) {
                val latestRefresh = runBlocking { preference.getRefreshToken() }
                    ?.trim()
                    ?.takeIf { it.isNotBlank() }
                val fallbackAccess = runBlocking { preference.getAccessToken() }
                    ?.trim()
                    ?.takeIf { it.isNotBlank() }
                if (latestRefresh != null && latestRefresh != refreshToken && fallbackAccess != null) {
                    return response.request.withBearerToken(fallbackAccess)
                }
                clearSessionAndNotify()
                return null
            }

            val body = refreshResponse.body()
            if (body == null) {
                clearSessionAndNotify()
                return null
            }

            val newAccessToken = body.data.accessToken.trim().takeIf { it.isNotBlank() } ?: run {
                clearSessionAndNotify()
                return null
            }
            val newRefreshToken = body.data.refreshToken.trim().takeIf { it.isNotBlank() } ?: run {
                clearSessionAndNotify()
                return null
            }

            runBlocking {
                preference.saveAccessToken(newAccessToken)
                preference.saveRefreshToken(newRefreshToken)
            }

            return response.request.withBearerToken(newAccessToken)
        }
    }

    private fun Request.withBearerToken(token: String): Request {
        return newBuilder()
            .header("Authorization", "Bearer ${token.trim()}")
            .build()
    }

    private fun Request.bearerTokenOrNull(): String?{
        val raw = header("Authorization") ?: return null
        val prefix = "Bearer "
        val i = raw.indexOf(prefix, ignoreCase = true)
        if (i < 0) return null
        return raw.substring(startIndex = i + prefix.length).trim().takeIf { it.isNotBlank() }
    }
    private fun clearSessionAndNotify() {
        runBlocking { preference.clearSession() }
        sessionExpiredNotifier.notifySessionExpired()
    }

    private fun responseCount(response: Response): Int{
        var count = 1
        var current = response.priorResponse
        while (current != null){
            count++
            current = current.priorResponse
        }
        return count
    }

    private companion object {
        private val refreshMutex = Any()
    }
}