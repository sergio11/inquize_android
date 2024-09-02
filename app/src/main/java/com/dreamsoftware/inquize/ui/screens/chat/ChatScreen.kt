package com.dreamsoftware.inquize.ui.screens.chat

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.dreamsoftware.brownie.component.screen.BrownieScreen

data class ChatScreenArgs(
    val id: String
)

@Composable
fun ChatScreen(
    viewModel: ChatViewModel = hiltViewModel(),
    args: ChatScreenArgs,
    onBackPressed: () -> Unit
) {
    BrownieScreen(
        viewModel = viewModel,
        onBackPressed = onBackPressed,
        onPause = { onAssistantSpeechStopped() },
        onInitialUiState = { ChatUiState() },
        onInit = { load(args.id) },
        onSideEffect = {
            when(it) {
                ChatSideEffects.CloseChat -> onBackPressed()
            }
        }
    ) { uiState ->
        ChatScreenContent(
            uiState = uiState,
            actionListener = viewModel
        )
    }
}