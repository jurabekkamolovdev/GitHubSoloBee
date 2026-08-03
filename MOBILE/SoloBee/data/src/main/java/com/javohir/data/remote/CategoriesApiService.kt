package com.javohir.data.remote

import com.javohir.data.model.response.category.CategoriesResponse
import retrofit2.Response
import retrofit2.http.GET

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.remote
 * Description: Categories Retrofit Interface
 */
interface CategoriesApiService {

    @GET("courses/categories")
    suspend fun getCategories(): Response<CategoriesResponse>

}
