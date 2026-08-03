package com.javohir.domain.model

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.domain.model
 * Description: Avatar: ro'yxatdan o'tishda va profilda tanlanadigan avatar.
 */
data class Avatar(
    val id: String,
    val gender: AvatarGender,
    val thumbnailUrl: String,
    val orderIndex: Int
)