package com.javohir.feature.profile.content.common

import com.javohir.feature.R
import com.javohir.feature.profile.ProfileIntent

internal fun createProfileMenuItems(
    onAction: (ProfileIntent) -> Unit,
): List<ProfileMenuItemUi> {
    return listOf(
        ProfileMenuItemUi(
            iconRes = R.drawable.profile_ic,
            titleRes = R.string.profile,
            onClick = { onAction(ProfileIntent.ProfileSettingsClicked) },
        ),
        ProfileMenuItemUi(
            iconRes = R.drawable.language_ic,
            titleRes = R.string.language,
            trailingTextRes = R.string.language_english,
            onClick = { onAction(ProfileIntent.LanguageClicked) },
        ),
        ProfileMenuItemUi(
            iconRes = R.drawable.sound_ic,
            titleRes = R.string.sound,
            onClick = { onAction(ProfileIntent.SoundClicked) },
        ),
        ProfileMenuItemUi(
            iconRes = R.drawable.parent_ic,
            titleRes = R.string.parent_zone,
            onClick = { onAction(ProfileIntent.ParentZoneClicked) },
        ),
        ProfileMenuItemUi(
            iconRes = R.drawable.delete_ic,
            titleRes = R.string.delete,
            onClick = {onAction(ProfileIntent.DeleteAccountClicked)}
        )
    )
}
