package com.javohir.ui.component

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.ui.component
 * Description: CategoryCard: qayta foydalaniladigan UI komponent.
 */

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.SubcomposeAsyncImage
import com.javohir.ui.theme.SoloBeeColors

@Composable
fun CategoryCard(
    name: String,
    lessonCount: String,
    imageResList: List<String>,
    backgroundColor: Color,
    foregroundColor: Color,
    nameFont: FontFamily,
    lessonCountFont: FontFamily,
    modifier: Modifier = Modifier,
    @DrawableRes badgeIconRes: Int,
    onClick: () -> Unit
) {
    val imgs = imageResList.take(n = 3)
    val imageZoneH = 150.dp - 72.dp + 20.dp

    Box(
        modifier = modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth()
            .height(150.dp)
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(24.dp), clip = false)
            .background(backgroundColor, RoundedCornerShape(24.dp))
            .clickable(onClick = onClick)
    ) {
        if (imgs.size >= 3) {
            BoxWithConstraints(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .height(imageZoneH)
                    .zIndex(1f)
                    .padding(horizontal = 4.dp)
            ) {
                CategoryNetworkImage(
                    imageUrl = imgs[0],
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset(x = maxWidth * 0.05f, y = -maxHeight * 0.06f)
                        .width(maxWidth * 0.35f)
                        .height(maxHeight * 0.94f)
                        .zIndex(0f)
                        .graphicsLayer {
                            rotationZ = -8f
                            transformOrigin = TransformOrigin(0.5f, 1f)
                        }
                        .clip(RoundedCornerShape(16.dp))
                )
                CategoryNetworkImage(
                    imageUrl = imgs[2],
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = -maxWidth * 0.05f, y = -maxHeight * 0.06f)
                        .width(maxWidth * 0.35f)
                        .height(maxHeight * 0.94f)
                        .zIndex(2f)
                        .graphicsLayer {
                            rotationZ = 8f
                            transformOrigin = TransformOrigin(0.5f, 1f)
                        }
                        .clip(RoundedCornerShape(16.dp))
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = -maxHeight * 0.15f)
                        .width(maxWidth * 0.38f)
                        .height(maxHeight * 0.98f)
                        .zIndex(1f)
                        .background(Color.White, RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                ) {
                    CategoryNetworkImage(
                        imageUrl = imgs[1],
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        } else if (imgs.size == 2) {
            val plateShape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp)
            BoxWithConstraints(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .height(imageZoneH)
                    .zIndex(1f)
                    .padding(horizontal = 4.dp)
            ) {
                val mw = maxWidth
                val mh = maxHeight
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                        .background(Color.White, plateShape)
                        .clip(plateShape)
                        .zIndex(0f)
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(1f)
                ) {
                    CategoryNetworkImage(
                        imageUrl = imgs[0],
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .offset(x = mw * 0.11f, y = -mh * 0.14f)
                            .width(mw * 0.44f)
                            .height(mh * 0.94f)
                            .graphicsLayer {
                                rotationZ = -8f
                                transformOrigin = TransformOrigin(0.5f, 1f)
                            }
                            .clip(RoundedCornerShape(16.dp))
                    )
                    CategoryNetworkImage(
                        imageUrl = imgs[1],
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = -mw * 0.11f, y = -mh * 0.14f)
                            .width(mw * 0.44f)
                            .height(mh * 0.94f)
                            .graphicsLayer {
                                rotationZ = 8f
                                transformOrigin = TransformOrigin(0.5f, 1f)
                            }
                            .clip(RoundedCornerShape(16.dp))
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .zIndex(4f)
                .height(72.dp)
                .background(foregroundColor, RoundedCornerShape(16.dp))
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = name,
                style = TextStyle(
                    color = SoloBeeColors.White,
                    fontSize = 22.sp,
                    fontFamily = nameFont
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
            )
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(backgroundColor.darken(0.58f))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Image(
                    painter = painterResource(badgeIconRes),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.Fit
                )
                Text(
                    text = lessonCount,
                    style = TextStyle(
                        color = SoloBeeColors.White,
                        fontSize = 12.sp,
                        fontFamily = lessonCountFont
                    )
                )
            }
        }
    }
}

@Composable
private fun CategoryNetworkImage(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    SubcomposeAsyncImage(
        model = imageUrl,
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop,
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .shimmerSkeleton(
                        visible = true,
                        shape = RoundedCornerShape(16.dp)
                    )
            )
        }
    )
}

private fun Color.darken(factor: Float): Color = Color(
    red = (red * factor).coerceIn(0f, 1f),
    green = (green * factor).coerceIn(0f, 1f),
    blue = (blue * factor).coerceIn(0f, 1f),
    alpha = alpha
)
