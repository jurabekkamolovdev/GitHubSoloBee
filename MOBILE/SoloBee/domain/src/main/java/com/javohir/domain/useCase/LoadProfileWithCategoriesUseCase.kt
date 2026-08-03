package com.javohir.domain.useCase
import com.javohir.domain.common.awaitLoaded
import com.javohir.domain.model.ProfileWithCategories
import com.javohir.domain.repository.HomeRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.domain.useCase
 * Description: Asosiy sahifa va kategoriya osti ekranlari uchun talaba profili bilan kategoriyalarni bir vaqtda yuklash.
 */
class LoadProfileWithCategoriesUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
) {

    suspend operator fun invoke(forceRefresh: Boolean): ProfileWithCategories = coroutineScope {
        val profile = async {
            homeRepository.getStudentProfile(forceRefresh = forceRefresh).awaitLoaded()
        }
        val categories = async {
            homeRepository.getCategories(forceRefresh = forceRefresh).awaitLoaded()
        }
        ProfileWithCategories(profile = profile.await(), categories = categories.await())
    }
}
