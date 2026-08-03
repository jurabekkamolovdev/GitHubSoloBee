package com.javohir.feature.home.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.javohir.feature.R
import com.javohir.feature.home.HomeIntent
import com.javohir.feature.home.HomeState
import com.javohir.feature.common.ProfileHeader
import com.javohir.ui.component.CategoryCard

@Composable
fun PhoneContent(
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
            navigateToProfile = {onAction(HomeIntent.NavigateToProfile)}
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(top = 28.dp, bottom = 32.dp),
        ) {
            items(
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
