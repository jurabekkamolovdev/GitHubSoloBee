package com.javohir.domain.repository

import com.javohir.domain.common.Resource
import com.javohir.domain.model.LoginResult

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.domain.repository
 * Description: Repository Interface
 */
interface LoginRepository {

    suspend fun login(username: String, password: String): Resource<LoginResult>

}