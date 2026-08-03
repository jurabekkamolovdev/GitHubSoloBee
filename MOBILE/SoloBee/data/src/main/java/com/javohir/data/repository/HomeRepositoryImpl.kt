package com.javohir.data.repository
import com.javohir.data.mapper.category.toDomain as categoryToDomain
import com.javohir.data.mapper.profile.toDomain as profileToDomain
import com.javohir.data.remote.CategoriesApiService
import com.javohir.data.remote.ProfileApiService
import com.javohir.data.utils.invalidatesStudentSession
import com.javohir.data.utils.parseApiErrorBodyMessage
import com.javohir.domain.common.Resource
import com.javohir.domain.model.Profile
import com.javohir.domain.repository.HomeRepository
import com.javohir.domain.repository.SessionRepository
 import com.javohir.domain.model.Category
import com.javohir.utils.AppLogger
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.repository
 * Description: HomeRepository implementation
 */


class HomeRepositoryImpl @Inject constructor(
    private val profileApi: ProfileApiService,
    private val categoriesApi: CategoriesApiService,
    private val sessionRepository: SessionRepository,
    private val logger: AppLogger
) : HomeRepository {

    private val profileMutex = Mutex()
    private val categoriesMutex = Mutex()

    private val _cashedProfile = MutableStateFlow<Profile?>(value = null)
    override val cashedProfile: StateFlow<Profile?> = _cashedProfile.asStateFlow()

    private val _cashedCategories = MutableStateFlow<List<Category>?>(value = null)
    override val cashedCategories: StateFlow<List<Category>?> = _cashedCategories.asStateFlow()


    override suspend fun getStudentProfile(forceRefresh: Boolean): Flow<Resource<Profile>> = flow {
        if (!forceRefresh) {
            _cashedProfile.value?.let {
                emit(Resource.Success(data = it))
                return@flow
            }
        }


        emit(value = Resource.Loading)

        profileMutex.withLock {
            if (!forceRefresh && _cashedProfile.value != null) {
                emit(Resource.Success(data = _cashedProfile.value!!))
                return@flow
            }

            val response = try {
                profileApi.getStudentProfile()
            } catch (e: Exception) {
                logger.logError(e)
                logger.log("HomData(API: students/profile) ${e.message}")
                emit(value = Resource.Error(e.message ?: "Unknown error"))
                return@flow
            }

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    _cashedProfile.value = body.profileToDomain()
                    emit(value = Resource.Success(data = body.profileToDomain()))
                } else {
                    emit(value = Resource.Error("Empty body. code=${response.code()}"))
                }
            } else {
                if (response.code().invalidatesStudentSession()) {
                    clearCachedProfile()
                    sessionRepository.clearSessionAndNotify()
                    emit(value = Resource.Unauthorized)
                    return@flow
                }
                val detail = response.errorBody()?.string()?.parseApiErrorBodyMessage() ?: response.message()
                emit(value = Resource.Error("Profile failed: code=${response.code()} $detail"))
            }
        }
    }
    override suspend fun getCategories(forceRefresh: Boolean): Flow<Resource<List<Category>>> {
        return flow {

            if (!forceRefresh){
                _cashedCategories.value?.let {
                    emit(Resource.Success(data = it))
                    return@flow
                }
            }

            emit(Resource.Loading)

            categoriesMutex.withLock {
                if (!forceRefresh && _cashedCategories.value != null) {
                    emit(Resource.Success(data = _cashedCategories.value!!))
                    return@flow
                }

                val response = try {
                    categoriesApi.getCategories()
                } catch (e: Exception) {
                    logger.logError(e)
                    logger.log("HomeCategories(API: courses categories) ${e.message}")
                    emit(value = Resource.Error("${e.message}"))
                    return@flow
                }

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        _cashedCategories.value = body.categoryToDomain()
                        emit(value = Resource.Success(data = body.categoryToDomain()))
                    } else {
                        emit(value = Resource.Error("Empty body. code=${response.code()}"))
                    }
                } else {
                    val detail = response.errorBody()?.string()?.parseApiErrorBodyMessage() ?: response.message()
                    emit(value = Resource.Error("Category failed: code=${response.code()} $detail"))
                }
            }
        }
    }

    override fun clearCachedProfile() {
        _cashedProfile.value = null
    }
}

