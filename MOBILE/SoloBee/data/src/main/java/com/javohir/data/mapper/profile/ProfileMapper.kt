package com.javohir.data.mapper.profile

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.mapper.profile
 * Description: ProfileMapper: network modeldan domain mapping.
 */

import com.javohir.data.model.response.profile.ProfileResponse
import com.javohir.domain.model.Profile as DomainProfile

private const val DEFAULT_SCORE = "0"

fun ProfileResponse.toDomain(): DomainProfile {
    return DomainProfile(
        firstName = data?.firstName.orEmpty(),
        lastName = data?.lastName.orEmpty(),
        // Ball yo'q bo'lsa nol ko'rsatiladi — domain modeli non-null bo'lib qoladi
        score = data?.score?.takeIf { it.isNotBlank() } ?: DEFAULT_SCORE,
    )
}
