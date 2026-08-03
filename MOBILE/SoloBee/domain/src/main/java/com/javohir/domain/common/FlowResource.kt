package com.javohir.domain.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.domain.common
 * Description: Flow Resource: parallel soʻrovlar/use case lar uchun birinchi Loadingdan keyingi Resource.
 */
suspend fun <T> Flow<Resource<T>>.awaitLoaded(): Resource<T> = first { it !is Resource.Loading }
