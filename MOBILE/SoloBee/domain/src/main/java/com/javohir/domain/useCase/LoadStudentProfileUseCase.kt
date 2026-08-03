package com.javohir.domain.useCase

import com.javohir.domain.common.Resource
import com.javohir.domain.common.awaitLoaded
import com.javohir.domain.model.Profile
import com.javohir.domain.model.ProfileScreenData
import com.javohir.domain.repository.HomeRepository
import javax.inject.Inject

class LoadStudentProfileUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
) {
    suspend operator fun invoke(forceRefresh: Boolean = false): Resource<ProfileScreenData> {
        return when (val result = homeRepository.getStudentProfile(forceRefresh = forceRefresh).awaitLoaded()) {
            is Resource.Success -> Resource.Success(result.data.toScreenData())
            is Resource.Error -> Resource.Error(result.message)
            is Resource.Unauthorized -> Resource.Unauthorized
            is Resource.Loading -> Resource.Error("Unexpected loading")
        }
    }

    private fun Profile.toScreenData(): ProfileScreenData {
        val fullName: String = listOf(firstName, lastName)
            .filter { it.isNotBlank() }
            .joinToString(separator = " ")
        val userInitial: String = firstName.trim().firstOrNull()?.uppercaseChar()?.toString().orEmpty()
        return ProfileScreenData(
            fullName = fullName,
            userInitial = userInitial,
            score = score,
        )
    }
}
