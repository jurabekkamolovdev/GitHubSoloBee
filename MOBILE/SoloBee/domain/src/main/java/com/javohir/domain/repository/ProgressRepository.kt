package com.javohir.domain.repository

import com.javohir.domain.common.Resource
import com.javohir.domain.model.ProgressResult
import kotlinx.coroutines.flow.Flow

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.domain.repository
 * Description: repository interface
 */
interface ProgressRepository {

    /** [result] — foydalanuvchi tergan matn (WRITING). Boshqa turlarda bo'sh. */
    suspend fun progress(activityId: String, result: String = ""): Flow<Resource<ProgressResult>>

}
