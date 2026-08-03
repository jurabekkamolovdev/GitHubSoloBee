package com.javohir.feature.profile

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.profile
 * Description: UI State
 */
data class ProfileState(
    val isLoadingProfile: Boolean = false,
    val isDeletingAccount: Boolean = false,
    val showDeleteAccountDialog: Boolean = false,
    val userInitial: String = "",
    val fullName: String = "",
)