package com.javohir.data.utils

import com.javohir.domain.common.Resource
import org.json.JSONObject
import retrofit2.Response

/**
 * Created by: Javohir Oromov macOS
 * Project: SoloBee
 * Package: com.javohir.data.utils
 * Description: API xato tanasi JSONini parse qilish; Retrofit Response dan domain Resource ga map.
 */

fun String.parseApiErrorBodyMessage(): String = runCatching {
    val root = JSONObject(this)
    root.optJSONObject("error")?.optString("message")
        ?.takeIf { it.isNotBlank() }
        ?: root.optString("message").takeIf { it.isNotBlank() }
        ?: this
}.getOrElse { this }

fun <D : Any, T> Response<D>.toResource(
    failureLabel: String,
    mapBody: (D) -> T,
): Resource<T> {
    if (!isSuccessful) {
        val detail = errorBody()?.string()?.parseApiErrorBodyMessage() ?: message()
        return Resource.Error("$failureLabel failed. code=${code()} $detail")
    }
    val body = body()
    return if (body != null) {
        Resource.Success(mapBody(body))
    } else {
        Resource.Error("Error body. code = ${code()}")
    }
}