package com.dreamsoftware.inquize.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.dreamsoftware.inquize.ui.screens.app.AppScreen
import com.dreamsoftware.inquize.ui.theme.InquizeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen() // Complying with Android 12 Splash Screen guidelines
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InquizeTheme {
                AppScreen()
            }
        }
    }
}
