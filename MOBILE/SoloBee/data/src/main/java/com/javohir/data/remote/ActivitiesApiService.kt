package com.javohir.data.remote

import com.javohir.data.model.response.activity.ActivitiesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.remote
 * Description: retfofit interface
 */

interface ActivitiesApiService {

    @GET(value = "courses/topics/{topicId}/activities")
    suspend fun getActivities(
        @Path(value = "topicId") topicId: String
    ): Response<ActivitiesResponse>


}