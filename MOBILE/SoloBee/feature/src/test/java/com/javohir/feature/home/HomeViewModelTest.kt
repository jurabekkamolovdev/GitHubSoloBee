package com.javohir.feature.home

import com.javohir.domain.common.Resource
import com.javohir.domain.model.Category
import com.javohir.domain.model.Profile
import com.javohir.domain.model.SubCategory
import com.javohir.domain.useCase.LoadProfileWithCategoriesUseCase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.home
 * Description: ViewModel Test
 */
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()


    @Test
    fun `init success bolsa state togri boladi`() = runTest {
        val repo = FakeHomeRepository().apply {
            homeDataFlow = flowOf(value = Resource.Success(data = Profile(firstName = "Javohir", lastName = "Oromov", score = "1200")))
            categoriesFlow = flowOf(
                value = Resource.Success(
                    data = listOf(
                        Category(
                            id = "1",
                            images = listOf("img1"),
                            foregroundColor = "#FFFFFF",
                            backgroundColor = "#000000",
                            name = "Math",
                            lessonCount = 26,
                            subCategory = listOf(
                                SubCategory(
                                    id = "11",
                                    categoryId = "1",
                                    name = "Algebra",
                                    imageUrl = "img-sub-1"
                                )
                            )
                        )
                    )
                )
            )
        }

        val vm = HomeViewModel(
            loadProfileWithCategoriesUseCase = LoadProfileWithCategoriesUseCase(homeRepository = repo),
        )

        advanceUntilIdle()

        val state = vm.state.value
        assertEquals("Javohir", state.firstName)
        assertEquals("J", state.userInitial)
        assertEquals("1200", state.score)
        assertEquals(1,state.categories.size)
        assertFalse(state.isLoadingProfile)
        assertFalse(state.isLoadingCategories)
        assertFalse(state.isRefreshing)
    }
}