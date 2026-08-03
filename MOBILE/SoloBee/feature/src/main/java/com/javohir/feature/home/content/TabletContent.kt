package com.javohir.feature.home.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as lazyGridItems
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.javohir.feature.R
import com.javohir.feature.home.HomeIntent
import com.javohir.feature.home.HomeState
import com.javohir.feature.common.ProfileHeader
import com.javohir.ui.component.CategoryCard

@Composable
fun TabletContent(
    paddingValues: PaddingValues,
    state: HomeState,
    onAction: (HomeIntent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding()),
    ) {
        ProfileHeader(
            userInitial = state.userInitial,
            firstName = state.firstName,
            score = state.score,
            isLoadingProfile = state.isLoadingProfile,
            navigateToProfile = {onAction(HomeIntent.NavigateToProfile)},
            avatarSize = 56.dp,
            avatarInitialFontSize = 32.sp,
            greetingNameSpacing = 16.dp,
            firstNameMaxWidthFraction = 0.4f,
            firstNameHeight = 24.dp,
            scoreRowHeight = 52.dp,
            scoreBadgeHeight = 40.dp,
            scoreBadgeCornerRadius = 20.dp,
            scoreBadgeStartPadding = 56.dp,
            scoreFontSize = 28.sp,
            scoreFontRes = R.font.baloo2_semibold,
            trophyIconRes = R.drawable.badge_ic,
            trophySize = 52.dp,
            trophyOffsetX = (-6).dp,
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 20.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            lazyGridItems(
                items = state.categories,
                key = { it.id },
            ) { item ->
                CategoryCard(
                    name = item.name,
                    lessonCount = item.lessonCount,
                    imageResList = item.images,
                    backgroundColor = item.backgroundColor,
                    foregroundColor = item.foregroundColor,
                    nameFont = FontFamily(Font(resId = R.font.baloo2_semibold)),
                    lessonCountFont = FontFamily(Font(resId = R.font.nunito_semibold)),
                    onClick = { onAction(HomeIntent.CategoryClicked(categoryId = item.id)) },
                    badgeIconRes = R.drawable.badge_ic,
                )
            }
        }
    }
}
