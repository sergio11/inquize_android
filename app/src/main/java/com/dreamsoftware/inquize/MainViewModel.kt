package com.dreamsoftware.inquize

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dreamsoftware.inquize.data.local.preferences.UserPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferencesManager: UserPreferencesManager
) : ViewModel() {

    /**
     * A [MutableStateFlow] that emits a boolean value indicating whether the welcome screen
     * should be displayed or not.
     */
    private val _shouldDisplayWelcomeScreenStream = MutableStateFlow<Boolean?>(null)

    /**
     * A [StateFlow] that emits a boolean value indicating whether the welcome screen
     * should be displayed or not.
     */
    val shouldDisplayWelcomeScreenStream = _shouldDisplayWelcomeScreenStream.asStateFlow()

    init {
        preferencesManager
            .preferencesStream
            .onEach { preferences ->
                _shouldDisplayWelcomeScreenStream.value = preferences.shouldShowWelcomeScreen
            }.launchIn(viewModelScope)
    }


    fun onNavigateToHomeScreen() {
        viewModelScope.launch {
            withContext(NonCancellable) { preferencesManager.setWelcomeScreenVisibilityStatus(false) }
        }
    }

}