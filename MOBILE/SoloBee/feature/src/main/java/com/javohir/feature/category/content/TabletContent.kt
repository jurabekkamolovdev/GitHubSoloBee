package com.javohir.feature.category.content

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.category.content
 * Description: category/TabletContent: qurilma o'lchami bo'yicha Compose kontent.
 */

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as lazyGridItems
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.javohir.feature.R
import com.javohir.feature.category.CategoryIntent
import com.javohir.feature.category.CategoryState
import com.javohir.feature.common.ProfileHeader
import com.javohir.ui.component.SubCategoryItem
import com.javohir.ui.component.shimmerSkeleton
import com.javohir.ui.theme.SoloBeeColors

@Composable
fun TabletContent(
    paddingValues: PaddingValues,
    state: CategoryState,
    onAction: (CategoryIntent) -> Unit,
) {
    val tabletContentMaxWidth = 980.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding())
            .padding(all = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .widthIn(max = tabletContentMaxWidth)
        ) {
            ProfileHeader(
                userInitial = state.userInitial,
                firstName = state.firstName,
                score = state.score,
                isLoadingProfile = state.isLoadingProfile,
                navigateToProfile = { onAction(CategoryIntent.NavigateToProfile) },
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
                scoreFontRes = R.font.baloo2_bold,
                trophyIconRes = R.drawable.trophy_ic,
                trophySize = 52.dp,
                trophyOffsetX = (-6).dp,
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 220.dp),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .widthIn(max = tabletContentMaxWidth)
                .weight(1f)
                .padding(top = 16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            lazyGridItems(
                items = state.subCategories,
                key = { it.id }
            ) { item ->
                SubCategoryItem(
                    name = item.subName,
                    imageUrl = item.image,
                    nameFont = FontFamily(Font(R.font.baloo2_semibold)),
                    imageSize = 176.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(246.dp),
                    onClick = {
                        onAction(
                            CategoryIntent.OpenTopics(
                                subCategoryId = item.id,
                                subCategoryName = item.subName,
                            ),
                        )
                    }
                )
            }
        }

        Box(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .padding(bottom = paddingValues.calculateBottomPadding())
                .fillMaxWidth(0.74f)
                .align(Alignment.CenterHorizontally)
                .height(176.dp)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(24.dp), clip = false)
                .background(color = state.footerBackgroundColor, RoundedCornerShape(24.dp))
                .clickable {}
        ) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SoloBeeColors.White)
            ) {}

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .height(92.dp),
                shape = RoundedCornerShape(size = 16.dp),
                colors = CardDefaults.cardColors(containerColor = state.footerForegroundColor)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = state.footerName,
                        style = TextStyle(
                            fontSize = 26.sp,
                            color = SoloBeeColors.White,
                            fontFamily = FontFamily(Font(R.font.baloo2_semibold))
                        )
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(size = 999.dp))
                            .background(color = SoloBeeColors.LessonsBackground)
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.badge_ic),
                            contentDescription = null,
                            modifier = Modifier.size(26.dp),
                            contentScale = ContentScale.Fit
                        )
                        Text(
                            text = state.footerLessonCount,
                            style = TextStyle(
                                color = SoloBeeColors.White,
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.nunito_semibold))
                            )
                        )
                    }
                }
            }
        }
    }
}
