package com.javohir.feature.home
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import org.junit.Rule
import org.junit.Test

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.home
 * Description: Home Content Test 
 */

class HomeContentTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun homeContent_showsUserAndCategory_onPhone(){
        val state = HomeState(
            firstName = "Javohir",
            userInitial = "J",
            score = "1200",
            categories = listOf(
                CategoryItem(
                    id = "1",
                    images = emptyList(),
                    foregroundColor = Color(0xFFE7187E),
                    backgroundColor = Color(0xFFB31E60),
                    name = "Math",
                    lessonCount = "Top"
                )
            )
        )
        composeRule.setContent {
            HomeContent(
                paddingValues = PaddingValues(all = 0.dp),
                isTablet = false,
                state = state,
                onAction = { _: HomeIntent -> }
            )
        }
        composeRule.onNodeWithText("Javohir").assertIsDisplayed()
        composeRule.onNodeWithText("1200").assertIsDisplayed()
        composeRule.onNodeWithText("Math").assertIsDisplayed()
        composeRule.onNodeWithText("Top").assertIsDisplayed()
    }
}