package com.javohir.data.remote

import com.javohir.data.model.request.RefreshRequest
import com.javohir.data.model.response.auth.refresh.RefreshResponse
import retrofit2.Response

class FakeRefreshApiService(
    private val response: Response<RefreshResponse>,
) : RefreshApiService {

    var refreshCallCount: Int = 0

    override suspend fun refreshToken(request: RefreshRequest): Response<RefreshResponse> {
        refreshCallCount++
        return response
    }
}
