package com.javohir.domain.repository

import com.javohir.domain.common.Resource

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.domain.repository
 * Description: Repository Interface
 */
interface RegisterRepository {

    suspend fun register(
        firstName: String,
        lastName: String,
        userName: String,
        password: String,
        age: Int,
        avatarId: String
    ): Resource<Unit>

}