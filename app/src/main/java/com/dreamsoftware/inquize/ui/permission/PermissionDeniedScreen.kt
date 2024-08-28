package com.dreamsoftware.inquize.ui.permission

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun PermissionsDeniedScreen() {
    val context = LocalContext.current
    val settingsIntent = remember {
        val uri = Uri.fromParts("package", context.packageName, null)
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(64.dp),
                imageVector = Icons.Outlined.Warning,
                contentDescription = null,
                tint = Color.White
            )
            Text(
                text = "Required permissions not granted",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "This app requires camera and microphone permissions to function properly." +
                        " Please grant these permissions from the settings app",
                style = MaterialTheme.typography.bodyMedium
            )
            Button(onClick = { context.startActivity(settingsIntent) }) {
                Text(text = "Grant permissions")
            }
        }
    }
}
