package com.dreamsoftware.inquize.data.local.preferences

import kotlinx.coroutines.flow.Flow

/**
 * Manages user preferences for the entire app in a persistent manner.
 */
interface UserPreferencesManager {
    /**
     * Stream of user preferences.
     */
    val preferencesStream: Flow<PerceiveAppPreferences>

    /**
     * Sets the assistant muted status.
     */
    suspend fun setAssistantMutedStatus(isAssistantMuted: Boolean)

    /**
     * Sets the welcome screen visibility status.
     */
    suspend fun setWelcomeScreenVisibilityStatus(shouldShowWelcomeScreen: Boolean)
}