package com.dreamsoftware.inquize.ui.screens.detail

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.hilt.navigation.compose.hiltViewModel
import com.dreamsoftware.brownie.component.screen.BrownieScreen

data class InquizeDetailScreenArgs(
    val id: String
)

@Composable
fun InquizeDetailScreen(
    viewModel: InquizeDetailViewModel = hiltViewModel(),
    args: InquizeDetailScreenArgs,
    onGoToChat: (String) -> Unit,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val scrollState: ScrollState = rememberScrollState(0)
    BrownieScreen(
        viewModel = viewModel,
        onBackPressed = onBackPressed,
        onInitialUiState = { InquizeDetailUiState() },
        onSideEffect = {
            when(it) {
                InquizeDetailSideEffects.CloseDetail -> onBackPressed()
                InquizeDetailSideEffects.InquizeDeleted -> onBackPressed()
                InquizeDetailSideEffects.OpenInquizeChat -> onGoToChat(args.id)
            }
        },
        onInit = { load(id = args.id) }
    ) { uiState ->
        InquizeDetailScreenContent(
            context = context,
            density = density,
            scrollState = scrollState,
            uiState = uiState,
            actionListener = viewModel
        )
    }
}
