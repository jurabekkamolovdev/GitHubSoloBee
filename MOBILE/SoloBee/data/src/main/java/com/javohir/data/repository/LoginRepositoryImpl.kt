package com.javohir.data.repository
import com.javohir.data.local.sharedPref.SharedPreference
import com.javohir.data.model.request.LoginRequest
import com.javohir.data.remote.LoginApiService
import com.javohir.domain.common.Resource
import com.javohir.domain.model.LoginResult
import com.javohir.domain.model.Tokens
import com.javohir.domain.repository.LoginRepository
import com.javohir.data.utils.parseApiErrorBodyMessage
import com.javohir.utils.AppLogger
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject


/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.repository
 * Description: AuthRepository implementation
 */


class LoginRepositoryImpl @Inject constructor(
    private val api: LoginApiService,
    private val logger: AppLogger,
    private val preference: SharedPreference
): LoginRepository {

    override suspend fun login(username: String, password: String): Resource<LoginResult> {

        return try {
            val response = api.login(request = LoginRequest(userName = username, password = password))

            if (response.isSuccessful) {
                val body = response.body() ?: return Resource.Error("Empty body. code = ${response.code()}")
                val accessToken = body.data.accessToken?.takeIf { it.isNotBlank() }
                val refreshToken = body.data.refreshToken?.takeIf { it.isNotBlank() }
                if (accessToken == null || refreshToken == null) {
                    return Resource.Error("Login response missing access or refresh token")
                }

                val result = LoginResult(
                    tokens = Tokens(
                        accessToken = accessToken,
                        refreshToken = refreshToken
                    )
                )
                preference.saveTokens(
                    accessToken = accessToken,
                    refreshToken = refreshToken,
                )
                preference.markOnboardingCompleted()
                Resource.Success(result)
            } else {
                val errorMessage = response.errorBody()?.string()?.parseApiErrorBodyMessage() ?: response.message()
                Resource.Error("Login failed: $errorMessage")
            }
        }catch (e: IOException){
            logger.logError(e)
            logger.log("${e.message}")
            Resource.Error("Internet connection error ${e.message}")
        }catch (e: HttpException){
            logger.logError(e)
            logger.log("${e.message}")
            Resource.Error("Server error: ${e.code()} ${e.message}")
        }catch (e: Exception){
            logger.logError(e)
            logger.log("${e.message}")
            Resource.Error("Unexpected error $e")
        }
    }
}