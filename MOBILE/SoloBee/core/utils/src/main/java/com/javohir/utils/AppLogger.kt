package com.javohir.utils

/**
 * Created by: Javohir Oromov macOS
 * Project: SoloBee
 * Package: com.javohir.utils
 * Description: Logger interface
 */

interface AppLogger{
    fun log(message: String)
    fun logError(throwable: Throwable)
}
