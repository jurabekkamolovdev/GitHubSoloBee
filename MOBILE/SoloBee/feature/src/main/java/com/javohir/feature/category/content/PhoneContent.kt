package com.javohir.feature.category.content

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.category.content
 * Description: category/PhoneContent: qurilma o'lchami bo'yicha Compose kontent.
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
fun PhoneContent(
    paddingValues: PaddingValues,
    state: CategoryState,
    onAction: (CategoryIntent) -> Unit,

) {
    val rows = state.subCategories.chunked(size = 2)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding())
            .padding(all = 16.dp)
    ) {
        ProfileHeader(
            userInitial = state.userInitial,
            firstName = state.firstName,
            score = state.score,
            isLoadingProfile = state.isLoadingProfile,
            navigateToProfile = { onAction(CategoryIntent.NavigateToProfile) },
        )
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(rows) { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowItems.forEach { item ->
                        SubCategoryItem(
                            name = item.subName,
                            imageUrl = item.image,
                            nameFont = FontFamily(Font(R.font.baloo2_semibold)),
                            modifier = Modifier
                                .weight(1f)
                                .height(220.dp),
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
                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .padding(bottom = paddingValues.calculateBottomPadding())
                .fillMaxWidth()
                .height(160.dp)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(24.dp), clip = false)
                .background(color = state.footerBackgroundColor, RoundedCornerShape(24.dp))
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
                    .height(88.dp),
                shape = RoundedCornerShape(size = 16.dp),
                colors = CardDefaults.cardColors(containerColor = state.footerForegroundColor)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = state.footerName,
                        style = TextStyle(
                            fontSize = 22.sp,
                            color = SoloBeeColors.White,
                            fontFamily = FontFamily(Font(R.font.baloo2_semibold))
                        )
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(size = 999.dp))
                            .background(color = SoloBeeColors.LessonsBackground)
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.badge_ic),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            contentScale = ContentScale.Fit
                        )
                        Text(
                            text = state.footerLessonCount,
                            style = TextStyle(
                                color = SoloBeeColors.White,
                                fontSize = 12.sp,
                                fontFamily = FontFamily(Font(R.font.nunito_semibold))
                            )
                        )
                    }
                }
            }
        }
    }
}
