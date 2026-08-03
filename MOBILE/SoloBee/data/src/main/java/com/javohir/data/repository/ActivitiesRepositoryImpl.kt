package com.javohir.data.repository

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.repository
 * Description: ActivitiesRepositoryImpl: `ActivitiesRepository` uchun repository implementatsiyasi.
 */

import com.javohir.data.mapper.activity.toDomain
import com.javohir.data.model.response.activity.ActivitiesResponse
import com.javohir.data.remote.ActivitiesApiService
import com.javohir.data.utils.toResource
import com.javohir.domain.common.Resource
import com.javohir.domain.model.Activity
import com.javohir.domain.repository.ActivitiesRepository
import com.javohir.utils.AppLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class  ActivitiesRepositoryImpl @Inject constructor(
    private val activitiesApi: ActivitiesApiService,
    private val logger: AppLogger,
) : ActivitiesRepository {

    override suspend fun getActivities(topicId: String): Flow<Resource<List<Activity>>> = flow {
        val response = try {
            activitiesApi.getActivities(topicId = topicId)
        } catch (e: Exception) {
            logger.log("ActivitiesRepository(API: activities) ${e.message}")
            logger.logError(throwable = e)
            emit(Resource.Error(message = "${e.message}"))
            return@flow
        }

        emit(
            response.toResource(failureLabel = "Activities") { body: ActivitiesResponse ->
                body.toDomain()
            },
        )
    }
}
