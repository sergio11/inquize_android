package com.dreamsoftware.inquize.ui.screens.app

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.dreamsoftware.brownie.component.screen.BrownieScreen
import com.dreamsoftware.brownie.utils.openSystemSettings
import com.dreamsoftware.inquize.ui.navigation.Screens
import com.dreamsoftware.inquize.ui.navigation.utils.navigateSingleTopTo
import com.dreamsoftware.inquize.ui.permission.PermissionsDeniedScreen

@Composable
fun AppScreen(
    viewModel: AppViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()
    var isRequiredPermissionsGranted by remember { mutableStateOf<Boolean?>(null) }
    val context = LocalContext.current
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
        onSideEffect = {
            when(it) {
                AppSideEffects.InvalidSession -> navController.navigateSingleTopTo(Screens.Onboarding.route)
                AppSideEffects.OpenSettings -> context.openSystemSettings()
            }
        },
        onInitialUiState = { AppUiState() }
    ) { uiState ->
        if (isRequiredPermissionsGranted == true) {
            AppScreenContent(
                navController = navController,
                uiState = uiState,
                actionListener = viewModel
            )
        } else if (isRequiredPermissionsGranted == false) {
            PermissionsDeniedScreen()
        }
    }
}