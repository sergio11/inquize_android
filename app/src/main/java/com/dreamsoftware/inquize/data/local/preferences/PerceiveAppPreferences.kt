package com.dreamsoftware.inquize.data.local.preferences

/**
 * Represents the user preferences for the Perceive app.
 *
 * @property isAssistantMuted boolean indicating whether the assistant is muted.
 * @property shouldShowWelcomeScreen boolean indicating whether the welcome screen should be shown.
 */
data class PerceiveAppPreferences(
    val isAssistantMuted: Boolean,
    val shouldShowWelcomeScreen: Boolean
)
