package com.javohir.data.remote

import com.javohir.data.model.response.profile.DeleteResponse
import retrofit2.Response
import retrofit2.http.DELETE

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.remote
 * Description: Delete Retrofit interface
 */
interface DeleteApiService {

    @DELETE(value = "students")
   suspend fun deleteStudent(): Response<DeleteResponse>

}