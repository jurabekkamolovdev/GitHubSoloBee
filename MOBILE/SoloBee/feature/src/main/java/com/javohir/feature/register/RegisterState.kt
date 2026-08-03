package com.javohir.feature.register

import com.javohir.domain.model.Avatar
import com.javohir.domain.model.AvatarGender

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.register
 * Description: UI State — barcha bosqichlar bitta state ustida ishlaydi.
 */
data class RegisterState(
    val step: RegisterStep = RegisterStep.PERSONAL_INFO,

    // 1-bosqich
    val firstName: String = "",
    val lastName: String = "",
    val age: String = "",

    // 2-bosqich
    val userName: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,

    // 3-bosqich
    val gender: AvatarGender = AvatarGender.BOY,
    val avatars: List<Avatar> = emptyList(),
    val selectedAvatarId: String? = null,
    val isAvatarsLoading: Boolean = false,

    val isLoading: Boolean = false,
) {
    /** Faol jins bo'yicha filtrlangan ro'yxat — Variant A. */
    val visibleAvatars: List<Avatar>
        get() = avatars.filter { avatar -> avatar.gender == gender }
}