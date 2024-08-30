package com.dreamsoftware.inquize.ui.screens.app

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.dreamsoftware.brownie.component.screen.BrownieScreen
import com.dreamsoftware.inquize.ui.permission.PermissionsDeniedScreen

@Composable
fun AppScreen(
    viewModel: AppViewModel = hiltViewModel(),
) {
    var isRequiredPermissionsGranted by remember { mutableStateOf<Boolean?>(null) }
    val permissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {
        val isCameraPermissionGranted = it[Manifest.permission.CAMERA] ?: false
        val isRecordAudioPermissionGranted = it[Manifest.permission.RECORD_AUDIO] ?: false
        isRequiredPermissionsGranted =
            isCameraPermissionGranted && isRecordAudioPermissionGranted
    }
    BrownieScreen(
        viewModel = viewModel,
        onResume = {
            permissionsLauncher.launch(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO
                )
            )
        },
        onInitialUiState = { AppUiState() }
    ) { uiState ->
        if (isRequiredPermissionsGranted == true) {
            AppScreenContent(
                uiState = uiState
            )
        } else if (isRequiredPermissionsGranted == false) {
            PermissionsDeniedScreen()
        }
    }
}