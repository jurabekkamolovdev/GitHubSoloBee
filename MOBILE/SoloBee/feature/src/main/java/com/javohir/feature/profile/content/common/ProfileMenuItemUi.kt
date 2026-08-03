package com.javohir.feature.profile.content.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

internal data class ProfileMenuItemUi(
    @DrawableRes val iconRes: Int,
    @StringRes val titleRes: Int,
    @StringRes val trailingTextRes: Int? = null,
    val onClick: () -> Unit,
)
