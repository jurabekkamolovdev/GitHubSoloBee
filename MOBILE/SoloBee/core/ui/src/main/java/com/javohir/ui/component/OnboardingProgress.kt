package com.javohir.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.javohir.ui.theme.SoloBeeColors

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.ui.component
 * Description: Progress Indicator
 */

@Composable
fun OnboardingProgress(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier
){
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount){ index ->
            val isActive = index == currentPage
            if (isActive){
              ActiveIndicator()
            }else{
                InactiveDot()
            }
        }
    }
}

@Composable
fun ActiveIndicator(
    modifier: Modifier = Modifier,
    outerColor: androidx.compose.ui.graphics.Color = SoloBeeColors.ProgressActiveOuter,
    innerColor: androidx.compose.ui.graphics.Color = SoloBeeColors.ProgressActiveInner
) {
    Box(
        modifier = modifier
            .height(12.dp)
            .width(44.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(outerColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(1.dp)
                .clip(RoundedCornerShape(999.dp))
                .background(innerColor)
        )
    }
}
@Composable
fun InactiveDot(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(10.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(SoloBeeColors.ProgressInactive)
    )
}