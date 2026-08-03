package com.javohir.solobee

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import com.javohir.data.local.sharedPref.SharedPreference
import com.javohir.data.session.SessionExpiredNotifier
import com.javohir.feature.R
import com.javohir.solobee.activity.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

@OptIn(ExperimentalTestApi::class)
@HiltAndroidTest
class SessionExpiredNavigationTest {

    private val hiltRule: HiltAndroidRule = HiltAndroidRule(this)

    private val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val ruleChain: RuleChain = RuleChain
        .outerRule(hiltRule)
        .around(composeTestRule)

    @Inject
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var sessionExpiredNotifier: SessionExpiredNotifier

    private val loginLabel: String
        get() = InstrumentationRegistry.getInstrumentation().targetContext.getString(R.string.login)

    private val onboardingTitle: String
        get() = InstrumentationRegistry.getInstrumentation().targetContext.getString(R.string.onboarding_title1)

    private val homeGreeting: String
        get() = InstrumentationRegistry.getInstrumentation().targetContext.getString(R.string.good_afternoon)

    @Before
    fun setUp() {
        hiltRule.inject()
        runBlocking {
            sharedPreference.resetAuthState()
        }
    }

    @Test
    fun sessionExpired_navigatesToLogin_whenOnboardingWasCompleted() {
        prepareSessionAndRelaunch {
            sharedPreference.saveTokens(accessToken = "test_access", refreshToken = "test_refresh")
            sharedPreference.markOnboardingCompleted()
        }
        waitForHomeScreen()
        simulateSessionExpired()
        waitForText(loginLabel)
        composeTestRule.onNodeWithText(loginLabel, useUnmergedTree = true).assertExists()
    }

    @Test
    fun sessionExpired_navigatesToOnBoarding_whenOnboardingWasNotCompleted() {
        prepareSessionAndRelaunch {
            sharedPreference.saveTokens(accessToken = "test_access", refreshToken = "test_refresh")
        }
        waitForHomeScreen()
        simulateSessionExpired()
        waitForText(onboardingTitle)
        composeTestRule.onNodeWithText(onboardingTitle, useUnmergedTree = true).assertExists()
    }

    private fun prepareSessionAndRelaunch(block: suspend () -> Unit) {
        runBlocking { block() }
        composeTestRule.activityRule.scenario.recreate()
        composeTestRule.waitForIdle()
    }

    private fun simulateSessionExpired() {
        runBlocking {
            sharedPreference.clearSession()
        }
        sessionExpiredNotifier.notifySessionExpired()
        composeTestRule.waitForIdle()
    }

    private fun waitForHomeScreen() {
        waitForText(homeGreeting)
    }

    private fun waitForText(text: String) {
        composeTestRule.waitUntil(timeoutMillis = NAVIGATION_TIMEOUT_MS) {
            composeTestRule.onAllNodesWithText(text, useUnmergedTree = true)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
    }

    private companion object {
        private const val NAVIGATION_TIMEOUT_MS: Long = 20_000L
    }
}
