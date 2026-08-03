package com.javohir.data.remote

import com.javohir.data.model.response.avatars.AvatarsResponse
import retrofit2.Response
import retrofit2.http.GET

/**
 * Created by: Javohir Oromov macOS
 * Project: SoloBee
 * Package: com.javohir.data.remote
 * Description: retrofit interface
 */
interface AvatarsApiService {

    @GET(value = "avatars")
    suspend fun getAvatars(): Response<AvatarsResponse>

}