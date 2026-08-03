package com.javohir.data.repository

import com.javohir.data.remote.DeleteApiService
import com.javohir.data.utils.parseApiErrorBodyMessage
import com.javohir.domain.common.Resource
import com.javohir.domain.repository.DeleteRepository
import com.javohir.utils.AppLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.repository
 * Description: Repository impelementation
 */
class DeleteRepositoryImpl @Inject constructor(
    private val deleteApi: DeleteApiService,
    private val logger: AppLogger
): DeleteRepository {

    override suspend fun deleteStudent(): Flow<Resource<Unit>> {
        return flow {
            emit(Resource.Loading)
            val response = try {
                deleteApi.deleteStudent()
            }catch (e: Exception){
                logger.log("DeleteRepository(API: Delete) ${e.message}")
                logger.logError(throwable = e)
                emit(Resource.Error(message = "${e.message}"))
                return@flow
            }
            if(response.isSuccessful){
                val body = response.body()
                if(body != null && body.data){
                    emit(Resource.Success(data = Unit))
                }else{
                    emit(Resource.Error("Empty body or delete failed. code=${response.code()}"))
                }
            }else{
                val detail = response.errorBody()?.string()?.parseApiErrorBodyMessage() ?: response.message()
                emit(Resource.Error("Delete failed: code=${response.code()} $detail"))
            }
        }
    }

}