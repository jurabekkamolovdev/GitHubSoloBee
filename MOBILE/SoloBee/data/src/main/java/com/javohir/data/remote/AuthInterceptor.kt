package com.javohir.data.remote

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.remote
 * Description: AuthInterceptor: HTTP client interceptor / autentifikatsiya zanjiri.
 */

import com.javohir.data.local.sharedPref.SharedPreference
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val preference: SharedPreference
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val path = originalRequest.url.encodedPath

        val isRefreshRequest = path.contains(other = "/auth/refresh")

        if (isRefreshRequest){
            return chain.proceed(originalRequest)
        }

        val token = runBlocking { preference.getAccessToken() }
        val request = chain.request().newBuilder().apply {
            if (!token.isNullOrBlank()) {
                header("Authorization", "Bearer $token")
            }
        }.build()
         return chain.proceed(request)
    }
}
