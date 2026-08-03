package com.javohir.data.remote

import com.javohir.data.model.response.profile.ProfileResponse
import retrofit2.Response
import retrofit2.http.GET

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.remote
 * Description: Profile Retrofit Interface
 */
interface ProfileApiService {

    @GET("students/profile")
    suspend fun getStudentProfile(): Response<ProfileResponse>
}
