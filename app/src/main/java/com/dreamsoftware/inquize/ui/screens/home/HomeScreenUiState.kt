package com.dreamsoftware.inquize.ui.screens.home

/**
 * A data class that represents the current UI state of the [HomeScreen].
 *
 * @property isListening Indicates whether the system is currently listening to the user.
 * @property hasErrorOccurred Indicates whether an error has occurred.
 */
data class HomeScreenUiState(
    val isListening: Boolean = false,
    val hasErrorOccurred: Boolean = false
)