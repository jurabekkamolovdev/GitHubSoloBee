package com.javohir.domain.repository
import com.javohir.domain.common.Resource
import com.javohir.domain.model.Category
import com.javohir.domain.model.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.domain.repository
 * Description: Repository interface
 */
interface HomeRepository {

    val cashedProfile: StateFlow<Profile?>
    val cashedCategories: StateFlow<List<Category>?>

    suspend fun getStudentProfile(forceRefresh: Boolean = false): Flow<Resource<Profile>>

    suspend fun getCategories(forceRefresh: Boolean = false): Flow<Resource<List<Category>>>

    fun clearCachedProfile()
}