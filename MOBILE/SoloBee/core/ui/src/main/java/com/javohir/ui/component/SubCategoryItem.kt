package com.javohir.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.javohir.ui.theme.SoloBeeColors

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.ui.component
 * Description: Sub Category Item
 */
@Composable
fun SubCategoryItem(
    name: String,
    nameFont: FontFamily,
    imageUrl: String,
    imageSize: Dp = 158.dp,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(SoloBeeColors.White)
            .clickable{onClick()}
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SubcomposeAsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(imageSize)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.FillWidth,
                loading = {
                    Box(
                        modifier = Modifier
                            .size(imageSize)
                            .clip(RoundedCornerShape(16.dp))
                            .shimmerSkeleton(
                                visible = true,
                                shape = RoundedCornerShape(16.dp)
                            )
                    )
                }
            )
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = name,
                style = TextStyle(
                    fontSize = 18.sp,
                    color = SoloBeeColors.Black,
                    fontFamily = nameFont
                ),
                maxLines = 1,
                textAlign = TextAlign.Center
            )
        }
    }
}