package com.javohir.data.mapper.progress

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.mapper.progress
 * Description: ProgressMapper: network modeldan domain mapping.
 */

import com.javohir.data.model.response.progress.ProgressData
import com.javohir.data.model.response.progress.ProgressResponse
import com.javohir.domain.model.ProgressResult

fun ProgressResponse.toDomain(): ProgressResult = data.toDomain()

fun ProgressData.toDomain(): ProgressResult = ProgressResult(
    attemptCount = attemptCount,
    threshold = threshold,
    completed = completed,
)
