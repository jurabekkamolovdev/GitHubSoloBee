package com.javohir.domain.repository

import com.javohir.domain.common.Resource
import com.javohir.domain.model.Topic
import kotlinx.coroutines.flow.Flow

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.domain.repository
 * Description: Repository interface
 */

interface TopicsRepository {

    suspend fun getTopics(subCategoryId: String): Flow<Resource<List<Topic>>>

}