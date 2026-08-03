package com.javohir.domain.common

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.domain.common
 * Description: common API response
 */

sealed class Resource<out T> {
    data class Success<T>(val data: T): Resource<T>()
    data class Error(val message: String): Resource<Nothing>()
    data object Unauthorized : Resource<Nothing>()
    object Loading: Resource<Nothing>()
}