package com.javohir.domain.repository

import com.javohir.domain.common.Resource
import com.javohir.domain.model.Avatar

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.domain.repository
 * Description: Repository Interface
 */
interface AvatarsRepository {

    suspend fun getAvatars(): Resource<List<Avatar>>

}