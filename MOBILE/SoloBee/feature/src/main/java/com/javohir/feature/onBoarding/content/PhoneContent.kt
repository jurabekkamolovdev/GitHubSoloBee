package com.javohir.feature.onBoarding.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.javohir.feature.R
import com.javohir.feature.onBoarding.OnBoardingIntent
import com.javohir.feature.onBoarding.OnBoardingPage
import com.javohir.ui.component.GlossyPrimaryButton
import com.javohir.ui.component.OnboardingProgress
import com.javohir.ui.theme.SoloBeeColors

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.onBoarding.content
 * Description: Phone Content 
 */
@Composable
fun PhoneContent(
    currentPage: OnBoardingPage,
    pageCount: Int,
    currentPageIndex: Int,
    maxHeight: Dp,
    onAction: (OnBoardingIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
            .padding(bottom = maxHeight * 0.03f),
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OnboardingProgress(
                pageCount = pageCount,
                currentPage = currentPageIndex,
                modifier = Modifier
            )
            TextButton(onClick = { onAction(OnBoardingIntent.SkipClicked) }) {
                Text(
                    text = stringResource(id = R.string.skip),
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = SoloBeeColors.White,
                        fontFamily = FontFamily(Font(resId = R.font.nunito_semibold))
                    )
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .widthIn(max = 1000.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SoloBeeColors.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = currentPage.title),
                        style = TextStyle(
                            fontSize = 24.sp,
                            color = SoloBeeColors.TextTitle,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(resId = R.font.baloo2_semibold))
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(id = currentPage.description),
                        style = TextStyle(
                            fontSize = 18.sp,
                            color = SoloBeeColors.TextTitle,
                            fontFamily = FontFamily(Font(resId = R.font.nunito_reguler))
                        ),
                        textAlign = TextAlign.Center
                    )
                }
                GlossyPrimaryButton(
                    text = stringResource(currentPage.buttonText),
                    fontFamily = FontFamily(Font(resId = R.font.baloo2_bold)),
                    onClick = { onAction(OnBoardingIntent.NextClicked) },
                    modifier = Modifier
                )
            }
        }
    }
}