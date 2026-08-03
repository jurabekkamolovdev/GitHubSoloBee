package com.javohir.data.mapper.avatar

import com.javohir.data.model.response.avatars.AvatarDto
import com.javohir.data.model.response.avatars.AvatarsResponse
import com.javohir.domain.model.Avatar
import com.javohir.domain.model.AvatarGender

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.mapper.avatar
 * Description: AvatarsMapper: response DTO larni domain modelga o'giradi.
 */

fun AvatarsResponse.toDomain(): List<Avatar> {
    val boy = data?.boy.orEmpty().mapNotNull { dto -> dto.toDomain(fallbackGender = AvatarGender.BOY) }
    val girl = data?.girl.orEmpty().mapNotNull { dto -> dto.toDomain(fallbackGender = AvatarGender.GIRL) }
    return boy + girl
}

private fun AvatarDto.toDomain(fallbackGender: AvatarGender): Avatar? {
    val avatarId = id?.takeIf { it.isNotBlank() } ?: return null
    val url = thumbnailUrl?.takeIf { it.isNotBlank() } ?: return null

    return Avatar(
        id = avatarId,
        gender = gender.toAvatarGender(fallback = fallbackGender),
        thumbnailUrl = url,
        orderIndex = orderIndex ?: 0
    )
}

private fun String?.toAvatarGender(fallback: AvatarGender): AvatarGender {
    return when (this?.uppercase()) {
        "BOY" -> AvatarGender.BOY
        "GIRL" -> AvatarGender.GIRL
        else -> fallback
    }
}