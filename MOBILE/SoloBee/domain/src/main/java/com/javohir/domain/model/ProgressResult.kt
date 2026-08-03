package com.javohir.domain.model

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.domain.model
 * Description: Progress API Result
 */
data class ProgressResult(
    val attemptCount: Int,
    val threshold: Int,
    val completed: String,
) {
    /** Server `completed` ni string ("true"/"false") ko'rinishida qaytaradi. */
    val isCompleted: Boolean get() = completed.equals(other = "true", ignoreCase = true)
}
