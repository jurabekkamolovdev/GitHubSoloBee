package com.javohir.domain.repository

import com.javohir.domain.common.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.domain.repository
 * Description: Repository interface
 */
interface DeleteRepository {

   suspend fun deleteStudent(): Flow<Resource<Unit>>

}