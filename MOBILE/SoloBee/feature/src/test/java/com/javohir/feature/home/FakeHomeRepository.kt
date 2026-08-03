package com.javohir.feature.home

import com.javohir.domain.common.Resource
import com.javohir.domain.model.Category
import com.javohir.domain.model.Profile
import com.javohir.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.home
 * Description: Fake Repository
 */
class FakeHomeRepository: HomeRepository {

    var homeDataFlow: Flow<Resource<Profile>> = flowOf(value = Resource.Loading)

    var categoriesFlow: Flow<Resource<List<Category>>> = flowOf(value = Resource.Loading)

    override val cashedProfile: StateFlow<Profile?> = MutableStateFlow(null)
    override val cashedCategories: StateFlow<List<Category>?> = MutableStateFlow(null)

    override suspend fun getStudentProfile(forceRefresh: Boolean): Flow<Resource<Profile>> {
        return homeDataFlow
    }

    override suspend fun getCategories(forceRefresh: Boolean): Flow<Resource<List<Category>>> {
        return categoriesFlow
    }

    override fun clearCachedProfile() {
    }
}