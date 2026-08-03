package com.javohir.domain.useCase

import com.javohir.domain.common.Resource
import com.javohir.domain.model.Avatar
import com.javohir.domain.repository.AvatarsRepository
import javax.inject.Inject

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.domain.useCase
 * Description: Avatarlar ro'yxatini olish va orderIndex bo'yicha tartiblash (ro'yxatdan o'tish va profil uchun).
 */
class GetAvatarsUseCase @Inject constructor(
    private val repository: AvatarsRepository
) {

    suspend operator fun invoke(): Resource<List<Avatar>> {
        return when (val result = repository.getAvatars()) {
            is Resource.Success -> Resource.Success(result.data.sortedBy { avatar -> avatar.orderIndex })
            else -> result
        }
    }
}