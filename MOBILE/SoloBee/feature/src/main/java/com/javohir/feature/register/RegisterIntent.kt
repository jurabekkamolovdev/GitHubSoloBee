package com.javohir.feature.register

import com.javohir.domain.model.AvatarGender

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.register
 * Description: User Action
 */
sealed class RegisterIntent {
    data class FirstNameChanged(val value: String): RegisterIntent()
    data class LastNameChanged(val value: String): RegisterIntent()
    data class AgeChanged(val value: String): RegisterIntent()
    data class UserNameChanged(val value: String): RegisterIntent()
    data class PasswordChanged(val value: String): RegisterIntent()
    data object TogglePasswordVisibility: RegisterIntent()
    data class GenderChanged(val gender: AvatarGender): RegisterIntent()
    data class AvatarSelected(val avatarId: String): RegisterIntent()
    data object ContinueClicked: RegisterIntent()
    data object BackClicked: RegisterIntent()
    data object SignUpClicked: RegisterIntent()
    data object SuccessShown: RegisterIntent()
    data object RetryAvatarsClicked: RegisterIntent()
}