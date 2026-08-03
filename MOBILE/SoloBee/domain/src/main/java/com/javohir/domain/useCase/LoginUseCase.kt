package com.javohir.domain.useCase

import com.javohir.domain.common.Resource
import com.javohir.domain.model.LoginResult
import com.javohir.domain.repository.LoginRepository
import javax.inject.Inject

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.domain.useCase
 * Description: Foydalanuvchi nomi va parol orqali tizimga kirish; JWT/sessiya saqlanadigan login operatsiyasi (repository orqali).
 */
class LoginUseCase @Inject constructor(
    private val repository: LoginRepository
) {

    suspend operator fun invoke(username: String, password: String): Resource<LoginResult>{
        return repository.login(username = username, password = password)
    }
}
