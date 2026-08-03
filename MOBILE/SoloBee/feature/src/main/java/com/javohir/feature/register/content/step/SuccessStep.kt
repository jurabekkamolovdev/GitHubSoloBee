package com.javohir.feature.register.content.step

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.register.content.step
 * Description: 4-bosqich — muvaffaqiyat ekrani, 2 soniyadan keyin login ekraniga o'tadi.
 */

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.javohir.feature.R
import kotlinx.coroutines.delay

private const val SUCCESS_DELAY_MILLIS = 2_000L

@Composable
fun SuccessStep(
    topPadding: Dp,
    onFinished: () -> Unit
) {
    val currentOnFinished by rememberUpdatedState(newValue = onFinished)

    LaunchedEffect(Unit) {
        delay(timeMillis = SUCCESS_DELAY_MILLIS)
        currentOnFinished()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = topPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.success_img),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(bottom = 48.dp)
        )
    }
}
@Composable
@Preview(showBackground = true)
fun SuccessStepPreview(){
    SuccessStep(
        topPadding = 12.dp,
        onFinished = {}
    )
}
