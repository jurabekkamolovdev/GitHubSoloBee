package com.javohir.data.remote

import com.javohir.data.model.request.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by: Javohir Oromov macOS
 * Project: SoloBee
 * Package: com.javohir.data.remote
 * Description: retrofit interface
 */

interface RegisterApiService {

    /**
     * Muvaffaqiyatli javobdagi `data` doim bo'sh `{}` — parse qilinadigan foydali ma'lumot yo'q.
     * `Response<Unit>` bilan Retrofit javob tanasini umuman o'qimaydi, shu sababli
     * javob shakli o'zgarsa ham ro'yxatdan o'tish buzilmaydi. Xato holatida
     * `errorBody()` baribir o'qiladi.
     */
    @POST(value = "students/register")
    suspend fun register(@Body request: RegisterRequest): Response<Unit>

}