package com.dreamsoftware.inquize.ui.chat

import com.dreamsoftware.inquize.domain.chat.ChatMessage

/**
 * Represents the state of the [ChatScreen].
 */
data class ChatScreenUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isListening: Boolean = false,
    val isLoading: Boolean = false,
    val hasErrorOccurred: Boolean = false,
    val isAssistantMuted: Boolean = false,
    val isAssistantSpeaking: Boolean = false
)