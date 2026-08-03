package com.javohir.feature.onBoarding
import androidx.annotation.StringRes
import com.javohir.feature.R


/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.onBoarding
 * Description: UI State 
 */
data class OnBoardingState(
    val pages: List<OnBoardingPage> = listOf(
        OnBoardingPage(
            image = R.drawable.onboarding_img1,
            title = R.string.onboarding_title1,
            description = R.string.onboarding_desc1,
            buttonText = R.string.onboarding_btn
        ),
        OnBoardingPage(
            image = R.drawable.onboarding_img2,
            title = R.string.onboarding_title2,
            description = R.string.onboarding_desc2,
            buttonText = R.string.onboarding_btn
        ),
        OnBoardingPage(
            image = R.drawable.onboarding_img3,
            title = R.string.onboarding_title3,
            description = R.string.onboarding_desc3,
            buttonText = R.string.onboarding_btn1
        )
    ),
    val currentPage: Int = 0
)
data class OnBoardingPage(
    val image: Int,
    @get:StringRes val title: Int,
    @get:StringRes val description: Int,
    @get:StringRes val buttonText: Int
)
