package com.dreamsoftware.inquize.ui.onboarding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.dreamsoftware.inquize.R
import com.dreamsoftware.inquize.ui.theme.PerceiveTheme

@Composable
fun WelcomeScreen(onNavigateToHomeScreenButtonClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Box(modifier = Modifier.weight(1f)) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier.size(128.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.ic_launcher_foreground),
                    contentDescription = null
                )
                Text(
                    text = "Welcome to Perceive",
                    style = MaterialTheme.typography.displaySmall
                )
                Text(
                    text = "Hi there! Welcome to Perceive! Find something interesting around you? Just point the camera, tap the mic and ask questions! It's that easy!",
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center
                )
            }
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            onClick = onNavigateToHomeScreenButtonClick,
            content = { Text(modifier = Modifier.padding(16.dp), text = "Let's get started!") }
        )
    }
}

@PreviewLightDark
@PreviewDynamicColors
@Composable
private fun WelcomeScreenPreview() {
    PerceiveTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
            content = { WelcomeScreen(onNavigateToHomeScreenButtonClick = {}) }
        )
    }
}