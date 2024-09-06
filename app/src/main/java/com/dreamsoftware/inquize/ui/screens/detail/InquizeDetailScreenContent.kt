package com.dreamsoftware.inquize.ui.screens.detail

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.dreamsoftware.brownie.component.screen.BrownieScreenContent
import com.dreamsoftware.inquize.ui.components.LoadingDialog

@Composable
internal fun InquizeDetailScreenContent(
    uiState: InquizeDetailUiState,
    actionListener: InquizeDetailScreenActionListener
) {
    with(uiState) {
        LoadingDialog(isShowingDialog = isLoading)
        BrownieScreenContent(
            hasTopBar = false,
            errorMessage = errorMessage,
            infoMessage = infoMessage,
            screenContainerColor = MaterialTheme.colorScheme.primary,
            enableVerticalScroll = false,
            onInfoMessageCleared = actionListener::onInfoMessageCleared,
            onErrorMessageCleared = actionListener::onErrorMessageCleared,
        ) {

        }
    }
}