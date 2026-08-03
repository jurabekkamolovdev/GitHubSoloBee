package com.javohir.feature.profile

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.profile
 * Description: User Intent
 */
sealed class ProfileIntent {
    data object ProfileSettingsClicked : ProfileIntent()
    data object LanguageClicked : ProfileIntent()
    data object SoundClicked : ProfileIntent()
    data object ParentZoneClicked : ProfileIntent()
    data object ChangeAvatarClicked : ProfileIntent()

    data object DeleteAccountClicked : ProfileIntent()
    data object DeleteAccountConfirmed : ProfileIntent()
    data object DeleteAccountDismissed : ProfileIntent()
}
