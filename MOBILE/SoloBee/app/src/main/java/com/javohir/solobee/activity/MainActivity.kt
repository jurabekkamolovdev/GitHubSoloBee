package com.javohir.solobee.activity

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.solobee.activity
 * Description: MainActivity: ilova asosiy aktivitisi (Compose setContent).
 */

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.javohir.solobee.navigation.AppNavGraph
import com.javohir.solobee.ui.theme.SoloBeeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        WindowCompat.setDecorFitsSystemWindows(window, false)
        @Suppress("DEPRECATION")
        window.navigationBarColor = Color.Transparent.toArgb()
        // Status bar ham kontent ustida chizilishi uchun (transparent kabi)
        window.statusBarColor = Color.Companion.Transparent.toArgb()
        WindowInsetsControllerCompat(window, window.decorView).run {
            isAppearanceLightNavigationBars = false
            isAppearanceLightStatusBars = true
        }

        val isTablet = resources.configuration.smallestScreenWidthDp >= 600
        requestedOrientation = if (isTablet) {
            ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        } else {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            val isTablet = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact
            SoloBeeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavGraph(
                        paddingValues = innerPadding,
                        isTablet = isTablet
                    )
                }
            }
        }
    }
}
