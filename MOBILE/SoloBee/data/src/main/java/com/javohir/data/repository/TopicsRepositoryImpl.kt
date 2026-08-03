package com.javohir.data.repository

import com.javohir.data.mapper.topic.toDomain
import com.javohir.data.model.response.topic.TopicsResponse
import com.javohir.data.remote.TopicsApiService
import com.javohir.data.utils.toResource
import com.javohir.domain.common.Resource
import com.javohir.domain.model.Topic
import com.javohir.domain.repository.TopicsRepository
import com.javohir.utils.AppLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.repository
 * Description: Repository Implementation
 */
class TopicsRepositoryImpl @Inject constructor(
    private val topicApi: TopicsApiService,
    private val logger: AppLogger
): TopicsRepository {
    override suspend fun getTopics(subCategoryId: String): Flow<Resource<List<Topic>>> {
        return flow {

            val response = try {
                topicApi.getTopics(subCategoryId = subCategoryId)
            } catch (e: Exception) {
                logger.log("TopicRepository(API: topics) ${e.message}")
                logger.logError(throwable = e)
                emit(value = Resource.Error(message = "${e.message}"))
                return@flow
            }

            emit(
                response.toResource(failureLabel = "Topics") { body: TopicsResponse ->
                    body.toDomain()
                },
            )
        }
    }


}