package com.javohir.data.remote

import com.javohir.data.model.request.RefreshRequest
import com.javohir.data.model.response.auth.refresh.RefreshResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.remote
 * Description: Retrofit interface
 */

interface RefreshApiService {

    @POST(value = "auth/refresh")
    suspend fun refreshToken(
        @Body request: RefreshRequest
    ): Response<RefreshResponse>

}