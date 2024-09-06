package com.dreamsoftware.inquize.ui.screens.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.dreamsoftware.brownie.component.screen.BrownieScreen

data class InquizeDetailScreenArgs(
    val id: String
)

@Composable
fun InquizeDetailScreen(
    viewModel: InquizeDetailViewModel = hiltViewModel(),
    args: InquizeDetailScreenArgs,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    BrownieScreen(
        viewModel = viewModel,
        onBackPressed = onBackPressed,
        onInitialUiState = { InquizeDetailUiState() }
    ) { uiState ->
        InquizeDetailScreenContent(
            uiState = uiState,
            actionListener = viewModel
        )
    }
}
