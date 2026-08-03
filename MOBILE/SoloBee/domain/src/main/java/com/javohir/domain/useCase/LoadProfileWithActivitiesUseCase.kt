package com.javohir.domain.useCase

import com.javohir.domain.common.awaitLoaded
import com.javohir.domain.model.ProfileWithActivities
import com.javohir.domain.repository.ActivitiesRepository
import com.javohir.domain.repository.HomeRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

/**
 * Created by: Javohir Oromov macOS
 * Project: SoloBee
 * Package: com.javohir.domain.useCase
 * Description: Learn/PicQuest/WordHunt kabi topic ekranlari uchun talaba profili va aktivitilar roʻyxatini parallel yuklash (application orchestration).
 */
class LoadProfileWithActivitiesUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
    private val activitiesRepository: ActivitiesRepository,
) {

    suspend operator fun invoke(topicId: String, forceRefresh: Boolean): ProfileWithActivities = coroutineScope {
        val profile = async {
            homeRepository.getStudentProfile(forceRefresh = forceRefresh).awaitLoaded()
        }
        val activities = async {
            activitiesRepository.getActivities(topicId = topicId).awaitLoaded()
        }
        ProfileWithActivities(profile = profile.await(), activities = activities.await())
    }
}
