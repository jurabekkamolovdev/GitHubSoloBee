package com.javohir.data.utils

internal fun Int.invalidatesStudentSession(): Boolean {
    return this == 400 || this == 401
}
