package com.javohir.data.repository

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.repository
 * Description: ProgressRepositoryImpl: `ProgressRepository` uchun repository implementatsiyasi.
 */

import com.javohir.data.mapper.progress.toDomain
import com.javohir.data.model.request.ProgressRequest
import com.javohir.data.model.response.progress.ProgressResponse
import com.javohir.data.remote.ProgressApiService
import com.javohir.data.utils.toResource
import com.javohir.domain.common.Resource
import com.javohir.domain.model.ProgressResult
import com.javohir.domain.repository.ProgressRepository
import com.javohir.utils.AppLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProgressRepositoryImpl @Inject constructor(
    private val progressApi: ProgressApiService,
    private val logger: AppLogger,
) : ProgressRepository {

    override suspend fun progress(
        activityId: String,
        result: String,
    ): Flow<Resource<ProgressResult>> = flow {
        val response = try {
            progressApi.progress(
                activityId = activityId,
                request = ProgressRequest(result = result),
            )
        } catch (e: Exception) {
            logger.log("ProgressRepository(API: progress) ${e.message}")
            logger.logError(throwable = e)
            emit(Resource.Error(message = "${e.message}"))
            return@flow
        }

        emit(
            response.toResource(failureLabel = "Progress") { body: ProgressResponse ->
                body.toDomain()
            },
        )
    }
}
