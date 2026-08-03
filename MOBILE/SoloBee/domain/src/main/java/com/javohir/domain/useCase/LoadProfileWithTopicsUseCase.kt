package com.javohir.domain.useCase

import com.javohir.domain.common.awaitLoaded
import com.javohir.domain.model.ProfileWithTopics
import com.javohir.domain.repository.HomeRepository
import com.javohir.domain.repository.TopicsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.domain.useCase
 * Description: Subkategoriya topics roʻyxati ekrani uchun profil (ball) va mavzular roʻyxatini parallel yuklash.
 */
class LoadProfileWithTopicsUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
    private val topicsRepository: TopicsRepository,
) {

    suspend operator fun invoke(subCategoryId: String, forceRefreshProfile: Boolean): ProfileWithTopics = coroutineScope {
        val profile = async {
            homeRepository.getStudentProfile(forceRefresh = forceRefreshProfile).awaitLoaded()
        }
        val topics = async {
            topicsRepository.getTopics(subCategoryId = subCategoryId).awaitLoaded()
        }
        ProfileWithTopics(profile = profile.await(), topics = topics.await())
    }
}
