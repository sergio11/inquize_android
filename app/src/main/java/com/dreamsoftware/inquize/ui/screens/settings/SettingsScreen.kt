package com.dreamsoftware.inquize.ui.screens.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.dreamsoftware.brownie.component.screen.BrownieScreen
import com.dreamsoftware.inquize.R
import com.dreamsoftware.inquize.ui.utils.shareApp

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    BrownieScreen(
        viewModel = viewModel,
        onInitialUiState = { SettingsUiState() },
        onSideEffect = {
            when (it) {
                SettingsUiSideEffects.ShareApp -> context.shareApp(R.string.share_message)
            }
        },
        onInit = {
            onInit()
        }
    ) { uiState ->
        SettingsScreenContent(
            uiState = uiState,
            actionListener = viewModel
        )
    }
}