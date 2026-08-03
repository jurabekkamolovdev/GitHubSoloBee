package com.javohir.domain.useCase
import com.javohir.domain.common.Resource
import com.javohir.domain.model.ProgressResult
import com.javohir.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.domain.useCase
 * Description: Berilgan aktivitet uchun progress yuborish va server javobini Resource oqimi (Flow) sifatida olish.
 */
class ProgressUseCase @Inject constructor(
    private val repository: ProgressRepository
) {

    suspend operator fun invoke(
        activityId: String,
        result: String = "",
    ): Flow<Resource<ProgressResult>> {
        return repository.progress(activityId = activityId, result = result)
    }
}

