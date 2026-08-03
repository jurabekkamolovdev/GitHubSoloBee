package com.javohir.data.remote

import com.javohir.data.model.request.LoginRequest
import com.javohir.data.model.response.auth.login.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.remote
 * Description: Retrofit Interface
 */
interface LoginApiService {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>
    
}