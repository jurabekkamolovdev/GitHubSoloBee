package com.javohir.data.remote

import com.javohir.data.session.SessionExpiredNotifier
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response as RetrofitResponse

class TokenAuthenticatorTest {

    @Test
    fun refreshReturns401_clearsSessionAndNotifies() = runBlocking {
        val tokenStore = FakeAuthTokenStore()
        val sessionExpiredNotifier = SessionExpiredNotifier()
        val refreshApiService = FakeRefreshApiService(
            response = RetrofitResponse.error(401, "".toResponseBody(null)),
        )
        val authenticator = TokenAuthenticator(
            preference = tokenStore,
            refreshApiService = refreshApiService,
            sessionExpiredNotifier = sessionExpiredNotifier,
        )
        val sessionExpiredWaiter = async {
            sessionExpiredNotifier.events.first()
        }
        val unauthorizedResponse: Response = buildUnauthorizedResponse(accessToken = "expired_access")
        val result: Request? = authenticator.authenticate(route = null, response = unauthorizedResponse)
        sessionExpiredWaiter.await()
        assertNull(result)
        assertTrue(tokenStore.isSessionCleared)
    }

    @Test
    fun refreshReturns401_doesNotNotify_whenAuthorizationHeaderMissing() {
        val tokenStore = FakeAuthTokenStore()
        val sessionExpiredNotifier = SessionExpiredNotifier()
        val refreshApiService = FakeRefreshApiService(
            response = RetrofitResponse.error(401, "".toResponseBody(null)),
        )
        val authenticator = TokenAuthenticator(
            preference = tokenStore,
            refreshApiService = refreshApiService,
            sessionExpiredNotifier = sessionExpiredNotifier,
        )
        val request: Request = Request.Builder()
            .url("https://api.solobee.uz/profile")
            .build()
        val response: Response = Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(401)
            .message("Unauthorized")
            .body("".toResponseBody(null))
            .build()
        val result: Request? = authenticator.authenticate(route = null, response = response)
        assertNull(result)
        assertTrue(!tokenStore.isSessionCleared)
    }

    private fun buildUnauthorizedResponse(accessToken: String): Response {
        val request: Request = Request.Builder()
            .url("https://api.solobee.uz/profile")
            .header("Authorization", "Bearer $accessToken")
            .build()
        return Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(401)
            .message("Unauthorized")
            .body("".toResponseBody(null))
            .build()
    }
}
