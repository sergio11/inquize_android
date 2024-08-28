package com.dreamsoftware.inquize.ui.screens.chat

import com.dreamsoftware.inquize.domain.model.ChatMessageBO

/**
 * Represents the state of the [ChatScreen].
 */
data class ChatScreenUiState(
    val messages: List<ChatMessageBO> = emptyList(),
    val isListening: Boolean = false,
    val isLoading: Boolean = false,
    val hasErrorOccurred: Boolean = false,
    val isAssistantMuted: Boolean = false,
    val isAssistantSpeaking: Boolean = false
)