package com.javohir.feature.register.content.step

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.register.content.step
 * Description: 3-bosqich — jins bo'yicha filtrlangan avatar tanlash.
 */

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.javohir.domain.model.Avatar
import com.javohir.domain.model.AvatarGender
import com.javohir.feature.R
import com.javohir.feature.register.RegisterIntent
import com.javohir.feature.register.RegisterState
import com.javohir.feature.register.content.common.RegisterLogo
import com.javohir.ui.component.GlossyPrimaryButton
import com.javohir.ui.component.RemoteImage
import com.javohir.ui.component.shimmerSkeleton
import com.javohir.ui.theme.SoloBeeColors

private val AvatarShape = RoundedCornerShape(16.dp)
private const val AVATAR_COLUMNS = 4
private const val SHIMMER_CELL_COUNT = 20

@Composable
fun SelectAvatarStep(
    topPadding: Dp,
    state: RegisterState,
    onAction: (RegisterIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = topPadding)
    ) {
        RegisterLogo(size = 100.dp)

        Text(
            text = stringResource(id = R.string.register_select_avatar_title),
            style = TextStyle(
                fontSize = 22.sp,
                fontFamily = FontFamily(Font(R.font.baloo2_bold)),
                color = SoloBeeColors.TextTitle
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        )

        GenderTabs(
            selected = state.gender,
            onSelect = { gender -> onAction(RegisterIntent.GenderChanged(gender)) },
            modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
        )

        Text(
            text = stringResource(
                id = if (state.gender == AvatarGender.BOY) {
                    R.string.register_boy_avatars
                } else {
                    R.string.register_girl_avatars
                }
            ),
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.nunito_semibold)),
                color = SoloBeeColors.TextTitle
            ),
            modifier = Modifier.padding(top = 16.dp, start = 16.dp)
        )

        AvatarGrid(
            state = state,
            onAvatarClick = { avatarId -> onAction(RegisterIntent.AvatarSelected(avatarId)) },
            modifier = Modifier
                .fillMaxWidth()
                .weight(weight = 1f)
        )

        GlossyPrimaryButton(
            text = stringResource(id = R.string.sign_up),
            fontFamily = FontFamily(Font(resId = R.font.baloo2_bold)),
            isLoading = state.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            onClick = { onAction(RegisterIntent.SignUpClicked) }
        )
    }
}

@Composable
private fun GenderTabs(
    selected: AvatarGender,
    onSelect: (AvatarGender) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(999.dp))
            .background(SoloBeeColors.GenderTabBackground)
            .padding(all = 6.dp)
    ) {
        GenderTab(
            text = stringResource(id = R.string.register_boy),
            iconRes = R.drawable.boy_ic,
            isSelected = selected == AvatarGender.BOY,
            onClick = { onSelect(AvatarGender.BOY) },
            modifier = Modifier.weight(weight = 1f)
        )
        GenderTab(
            text = stringResource(id = R.string.register_girl),
            iconRes = R.drawable.girl_ic,
            isSelected = selected == AvatarGender.GIRL,
            onClick = { onSelect(AvatarGender.GIRL) },
            modifier = Modifier.weight(weight = 1f)
        )
    }
}

@Composable
private fun GenderTab(
    text: String,
    @DrawableRes iconRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(999.dp))
            .background(
                if (isSelected) SoloBeeColors.GenderTabSelected else SoloBeeColors.GenderTabBackground
            )
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(size = 26.dp)
        )
        Spacer(modifier = Modifier.width(width = 8.dp))
        Text(
            text = text,
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.nunito_semibold)),
                color = if (isSelected) SoloBeeColors.TextTitle else SoloBeeColors.TextMuted
            )
        )
    }
}

@Composable
private fun AvatarGrid(
    state: RegisterState,
    onAvatarClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = AVATAR_COLUMNS),
        modifier = modifier,
        contentPadding = PaddingValues(all = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {
        if (state.isAvatarsLoading) {
            items(count = SHIMMER_CELL_COUNT) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(ratio = 1f)
                        .shimmerSkeleton(visible = true, shape = AvatarShape)
                )
            }
        } else {
            items(items = state.visibleAvatars, key = { avatar -> avatar.id }) { avatar ->
                AvatarCell(
                    avatar = avatar,
                    isSelected = avatar.id == state.selectedAvatarId,
                    onClick = { onAvatarClick(avatar.id) }
                )
            }
        }
    }
}

@Composable
private fun AvatarCell(
    avatar: Avatar,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val isBoy = avatar.gender == AvatarGender.BOY
    val cellColor = if (isBoy) SoloBeeColors.AvatarBoyBackground else SoloBeeColors.AvatarGirlBackground
    val borderColor = if (isBoy) SoloBeeColors.AvatarBoyBorder else SoloBeeColors.AvatarGirlBorder

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(ratio = 1f)
            .clip(AvatarShape)
            .background(cellColor)
            .border(
                // Tanlanmagan kataklarda ramka yo'q — dizayndagidek faqat rangli fon
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) borderColor else Color.Transparent,
                shape = AvatarShape
            )
            .clickable(onClick = onClick)
    ) {
        RemoteImage(
            imageUrl = avatar.thumbnailUrl,
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 6.dp),
            shape = AvatarShape
        )
    }
}