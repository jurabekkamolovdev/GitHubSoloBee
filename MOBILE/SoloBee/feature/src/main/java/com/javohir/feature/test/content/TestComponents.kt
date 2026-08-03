package com.javohir.feature.test.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.javohir.feature.R
import com.javohir.feature.test.TestIntent
import com.javohir.feature.test.TestState
import com.javohir.ui.component.RemoteImage
import com.javohir.ui.theme.SoloBeeColors

/**
 * Created by: Javohir Oromov macOS
 * Project: SoloBee
 * Package: com.javohir.feature.test.content
 * Description: Shared spell-mode widgets (typed word + letter options).
 */

private val LetterSlotColor = Color(0xFF94A3B8)

@Composable
fun TypedWordRow(
    state: TestState,
    onAction: (TestIntent) -> Unit,
    slotSize: Dp = 44.dp,
    fontSize: TextUnit = 28.sp,
) {
    val length = state.answer.length.coerceAtLeast(state.typed.size)
    if (length == 0) return

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(space = 10.dp, alignment = Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(length) { position ->
            val letter = state.typed.getOrNull(position)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable(enabled = letter != null) {
                    onAction(TestIntent.RemoveLetter(typedPosition = position))
                },
            ) {
                Text(
                    text = letter?.char.orEmpty(),
                    style = TextStyle(
                        fontSize = fontSize,
                        color = SoloBeeColors.Black,
                        fontFamily = FontFamily(Font(R.font.baloo2_bold)),
                        textAlign = TextAlign.Center,
                    ),
                    modifier = Modifier.height(slotSize),
                )
                Box(
                    modifier = Modifier
                        .width(slotSize)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(LetterSlotColor),
                )
            }
        }
    }
}

@Composable
fun LetterOptionsGrid(
    state: TestState,
    onAction: (TestIntent) -> Unit,
    columns: Int = 5,
    tileSpacing: Dp = 8.dp,
    imageFraction: Float = 0.5f,
    imagePaddingBottom: Dp = 5.dp,
    imagePaddingEnd: Dp = 5.dp,
) {
    if (state.options.isEmpty()) return

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(tileSpacing),
    ) {
        state.options.chunked(columns).forEach { rowOptions ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(tileSpacing),
            ) {
                rowOptions.forEach { option ->
                    LetterTile(
                        imageUrl = option.imageUrl.orEmpty(),
                        enabled = !option.used,
                        imageFraction = imageFraction,
                        imagePaddingBottom = imagePaddingBottom,
                        imagePaddingEnd = imagePaddingEnd,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .alpha(if (option.used) 0.3f else 1f),
                        onClick = { onAction(TestIntent.SelectLetter(index = option.index)) },
                    )
                }
                // Oxirgi qatordagi bo'sh kataklar — tugmalar bir xil o'lchamda qolishi uchun.
                repeat(times = columns - rowOptions.size) {
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                    )
                }
            }
        }
    }
}

/**
 * Bitta harf katagi: `testchar_bg` foni ustiga API'dan kelgan harf rasmi joylanadi
 * (TopicTile uslubida). Squircle pastki-o'ng tomonida soya borligi uchun rasm
 * `imagePaddingBottom`/`imagePaddingEnd` bilan biroz yuqori-chapga suriladi.
 */
@Composable
private fun LetterTile(
    imageUrl: String,
    enabled: Boolean,
    imageFraction: Float,
    modifier: Modifier = Modifier,
    imagePaddingBottom: Dp = 5.dp,
    imagePaddingEnd: Dp = 5.dp,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.testchar_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit,
        )
        RemoteImage(
            imageUrl = imageUrl,
            modifier = Modifier
                .fillMaxSize(fraction = imageFraction)
                .padding(bottom = imagePaddingBottom, end = imagePaddingEnd),
            contentScale = ContentScale.Fit,
            shape = RoundedCornerShape(8.dp),
        )
    }
}