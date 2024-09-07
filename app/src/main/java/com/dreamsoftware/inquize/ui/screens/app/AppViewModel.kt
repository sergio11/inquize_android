package com.dreamsoftware.inquize.ui.screens.app

import androidx.lifecycle.viewModelScope
import com.dreamsoftware.brownie.core.BrownieViewModel
import com.dreamsoftware.brownie.core.SideEffect
import com.dreamsoftware.brownie.core.UiState
import com.dreamsoftware.brownie.utils.BrownieEventBus
import com.dreamsoftware.brownie.utils.IBrownieAppEvent
import com.dreamsoftware.inquize.AppEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val appEventBus: BrownieEventBus
): BrownieViewModel<AppUiState, AppSideEffects>(), AppScreenActionListener {

    init {
        observeEvents()
    }

    override fun onGetDefaultState(): AppUiState = AppUiState()

    private fun observeEvents() {
        viewModelScope.launch {
            appEventBus.events.collect { event ->
                when(event) {
                    is AppEvent.SignOff -> launchSideEffect(AppSideEffects.InvalidSession)
                    is IBrownieAppEvent.NetworkConnectivityStateChanged ->
                        onNetworkConnectivityChanged(event.newState)
                }
            }
        }
    }

    private fun onNetworkConnectivityChanged(newState: Boolean) {
        updateState {
            it.copy(hasNetworkConnectivity = newState)
        }
    }

    override fun onOpenSettings() {
        launchSideEffect(AppSideEffects.OpenSettings)
    }
}

data class AppUiState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
    val hasNetworkConnectivity: Boolean = true
): UiState<AppUiState>(isLoading, errorMessage) {
    override fun copyState(isLoading: Boolean, errorMessage: String?): AppUiState =
        copy(isLoading = isLoading, errorMessage = errorMessage)
}

sealed interface AppSideEffects: SideEffect {
    data object InvalidSession: AppSideEffects
    data object OpenSettings: AppSideEffects
}