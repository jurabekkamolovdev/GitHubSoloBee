package com.javohir.domain.useCase

import com.javohir.domain.common.Resource
import com.javohir.domain.repository.RegisterRepository
import javax.inject.Inject

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.domain.useCase
 * Description: Yangi o'quvchini ro'yxatdan o'tkazish; matn maydonlari normallashtiriladi, javobda token qaytmaydi.
 */
class RegisterUseCase @Inject constructor(
    private val repository: RegisterRepository
) {

    suspend operator fun invoke(
        firstName: String,
        lastName: String,
        userName: String,
        password: String,
        age: Int,
        avatarId: String
    ): Resource<Unit> {
        return repository.register(
            firstName = firstName.trim(),
            lastName = lastName.trim(),
            userName = userName.trim(),
            password = password,
            age = age,
            avatarId = avatarId
        )
    }
}