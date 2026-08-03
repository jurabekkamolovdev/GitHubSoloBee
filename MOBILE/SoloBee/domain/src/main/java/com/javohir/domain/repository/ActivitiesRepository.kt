package com.javohir.domain.repository

import com.javohir.domain.common.Resource
import com.javohir.domain.model.Activity
import kotlinx.coroutines.flow.Flow

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.domain.repository
 * Description: repository interface
 */
interface ActivitiesRepository {

    suspend fun getActivities(topicId: String): Flow<Resource<List<Activity>>>
}
