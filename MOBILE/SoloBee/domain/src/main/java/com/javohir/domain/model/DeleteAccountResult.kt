package com.javohir.domain.model

sealed class DeleteAccountResult {
    data object Success : DeleteAccountResult()
    data class Error(val message: String) : DeleteAccountResult()
}
