package com.dreamsoftware.inquize

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dreamsoftware.inquize.ui.InquizeApp
import com.dreamsoftware.inquize.ui.theme.PerceiveTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            PerceiveTheme(content = { MainActivityContent() })
        }
    }

    @Composable
    private fun MainActivityContent() {
        val mainViewModel = hiltViewModel<MainViewModel>()
        val shouldShowWelcomeScreenState by mainViewModel
            .shouldDisplayWelcomeScreenStream
            .collectAsStateWithLifecycle()
        shouldShowWelcomeScreenState?.let { shouldShowWelcomeScreen ->
            PerceiveTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    InquizeApp(
                        shouldShowWelcomeScreen = shouldShowWelcomeScreen,
                        onNavigateToHomeScreen = mainViewModel::onNavigateToHomeScreen
                    )
                }
            }
        }
    }
}
