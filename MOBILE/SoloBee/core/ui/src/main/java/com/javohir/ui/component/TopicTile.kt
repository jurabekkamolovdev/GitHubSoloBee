package com.javohir.ui.component

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.ui.component
 * Description: TopicTile: qayta foydalaniladigan UI komponent.
 */

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage

@Composable
fun TopicTile(
    tileBackgroundResId: Int,
    imageUrl: String,
    clickable: Boolean,
    modifier: Modifier = Modifier,
    imagePaddingBottom: Dp = 5.dp,
    imagePaddingEnd: Dp = 5.dp,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(shape = RoundedCornerShape(size = 20.dp))
            .clickable(
                enabled = clickable,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = tileBackgroundResId),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
        )

        Box(
            modifier = Modifier
                .align(alignment = Alignment.Center)
                .fillMaxSize(fraction = 0.52f),
            contentAlignment = Alignment.Center,
        ) {
            SubcomposeAsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = imagePaddingBottom)
                    .padding(end = imagePaddingEnd),
                contentScale = ContentScale.Fit,
                alignment = Alignment.Center,
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = imagePaddingBottom)
                            .padding(end = imagePaddingEnd)
                            .shimmerSkeleton(
                                visible = true,
                                shape = RoundedCornerShape(size = 12.dp),
                            ),
                    )
                },
            )
        }
    }
}
