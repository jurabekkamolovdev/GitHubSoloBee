package com.javohir.data.repository

import com.javohir.data.model.request.RegisterRequest
import com.javohir.data.remote.RegisterApiService
import com.javohir.data.utils.parseApiErrorBodyMessage
import com.javohir.domain.common.Resource
import com.javohir.domain.repository.RegisterRepository
import com.javohir.utils.AppLogger
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.repository
 * Description: RegisterRepository implementation
 */
class RegisterRepositoryImpl @Inject constructor(
    private val api: RegisterApiService,
    private val logger: AppLogger
) : RegisterRepository {

    override suspend fun register(
        firstName: String,
        lastName: String,
        userName: String,
        password: String,
        age: Int,
        avatarId: String
    ): Resource<Unit> {
        return try {
            val response = api.register(
                request = RegisterRequest(
                    firstName = firstName,
                    lastName = lastName,
                    userName = userName,
                    password = password,
                    age = age,
                    avatarId = avatarId
                )
            )

            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                // Serverning o'z xabari foydalanuvchiga ko'rsatiladi: "username already exists" va h.k.
                val errorMessage = response.errorBody()?.string()?.parseApiErrorBodyMessage()
                logger.log("register failed. code=${response.code()} $errorMessage")
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
            logger.log("register unexpected: ${e.javaClass.simpleName} ${e.message}")
            Resource.Error("Kutilmagan xato")
        }
    }
}