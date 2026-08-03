package com.javohir.ui.component
/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.ui.component
 * Description: RemoteImage: qayta foydalaniladigan UI komponent.
 */

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage

@Composable
fun RemoteImage(
    imageUrl: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    shape: Shape = RoundedCornerShape(16.dp),
) {
    Box(
        modifier = modifier.clip(shape),
    ) {
        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .matchParentSize()
                .clip(shape),
            contentScale = contentScale,
            loading = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(shape)
                        .shimmerSkeleton(
                            visible = true,
                            shape = shape,
                        ),
                )
            },
        )
    }
}
