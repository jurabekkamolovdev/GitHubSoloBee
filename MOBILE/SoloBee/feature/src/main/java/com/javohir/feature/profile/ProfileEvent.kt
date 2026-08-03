package com.javohir.feature.profile

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.profile
 * Description: Event's
 */
sealed class ProfileEvent {
    data class ShowError(val message: String) : ProfileEvent()
    data object ShowComingSoon : ProfileEvent()
}