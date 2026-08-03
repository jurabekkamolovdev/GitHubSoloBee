package com.javohir.data.repository

import com.javohir.data.mapper.avatar.toDomain
import com.javohir.data.remote.AvatarsApiService
import com.javohir.data.utils.parseApiErrorBodyMessage
import com.javohir.domain.common.Resource
import com.javohir.domain.model.Avatar
import com.javohir.domain.repository.AvatarsRepository
import com.javohir.utils.AppLogger
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.repository
 * Description: AvatarsRepository implementation
 */
class AvatarsRepositoryImpl @Inject constructor(
    private val api: AvatarsApiService,
    private val logger: AppLogger
) : AvatarsRepository {

    override suspend fun getAvatars(): Resource<List<Avatar>> {
        return try {
            val response = api.getAvatars()

            if (response.isSuccessful) {
                val body = response.body()
                    ?: return Resource.Error("Avatarlarni yuklab bo'lmadi. code = ${response.code()}")

                Resource.Success(body.toDomain())
            } else {
                val errorMessage = response.errorBody()?.string()?.parseApiErrorBodyMessage()
                logger.log("getAvatars failed. code=${response.code()} $errorMessage")
                Resource.Error(errorMessage ?: response.message())
            }
        } catch (e: IOException) {
            logger.logError(e)
            logger.log("${e.message}")
            Resource.Error("Internet aloqasi yo'q")
        } catch (e: HttpException) {
            logger.logError(e)
            logger.log("${e.message}")
            Resource.Error("Server xatosi: ${e.code()}")
        } catch (e: Exception) {
            logger.logError(e)
            logger.log("${e.message}")
            Resource.Error("Kutilmagan xato")
        }
    }
}