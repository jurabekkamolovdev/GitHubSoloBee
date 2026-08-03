package com.javohir.data.remote

import com.javohir.data.model.response.topic.TopicsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.remote
 * Description: Retrofit interface
 */
interface TopicsApiService {

    @GET(value = "courses/subcategories/{subCategoryId}/topics")
   suspend fun getTopics(
        @Path(value = "subCategoryId") subCategoryId: String
    ): Response<TopicsResponse>
   
}