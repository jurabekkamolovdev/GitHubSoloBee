package com.javohir.domain.useCase

import com.javohir.domain.common.Resource
import com.javohir.domain.common.awaitLoaded
import com.javohir.domain.model.DeleteAccountResult
import com.javohir.domain.repository.DeleteRepository
import com.javohir.domain.repository.HomeRepository
import com.javohir.domain.repository.SessionRepository
import javax.inject.Inject

class DeleteStudentAccountUseCase @Inject constructor(
    private val deleteRepository: DeleteRepository,
    private val sessionRepository: SessionRepository,
    private val homeRepository: HomeRepository,
) {
    suspend operator fun invoke(): DeleteAccountResult {
        return when (val result = deleteRepository.deleteStudent().awaitLoaded()) {
            is Resource.Success -> {
                homeRepository.clearCachedProfile()
                sessionRepository.clearSessionAndNotify()
                DeleteAccountResult.Success
            }
            is Resource.Error -> DeleteAccountResult.Error(result.message)
            is Resource.Unauthorized -> DeleteAccountResult.Error("Unauthorized")
            is Resource.Loading -> DeleteAccountResult.Error("Unexpected loading")
        }
    }
}