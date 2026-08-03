package com.javohir.data.remote

import com.javohir.data.model.request.ProgressRequest
import com.javohir.data.model.response.progress.ProgressResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.remote
 * Description: retrofit interface
 */
interface ProgressApiService {

    @POST(value = "progress/activity/{id}")
    suspend fun progress(
        @Path(value = "id") activityId: String,
        @Body request: ProgressRequest,
    ): Response<ProgressResponse>

}