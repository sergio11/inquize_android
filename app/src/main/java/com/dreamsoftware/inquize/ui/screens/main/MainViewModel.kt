package com.dreamsoftware.inquize.ui.screens.main

import androidx.lifecycle.viewModelScope
import com.dreamsoftware.brownie.component.BottomNavBarItem
import com.dreamsoftware.brownie.core.BrownieViewModel
import com.dreamsoftware.brownie.core.SideEffect
import com.dreamsoftware.brownie.core.UiState
import com.dreamsoftware.inquize.R
import com.dreamsoftware.inquize.ui.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): BrownieViewModel<MainUiState, MainSideEffects>() {

    override fun onGetDefaultState(): MainUiState = MainUiState(mainDestinationList = listOf(
        BottomNavBarItem(
            route = Screens.Main.Home.Info.route,
            icon = R.drawable.icon_home,
            titleRes = R.string.home
        )
    ))

    fun onBottomItemsVisibilityChanged(hideBottomItems: Boolean) {
        viewModelScope.launch {
            updateState {
                it.copy(shouldShowBottomNav = !hideBottomItems)
            }
        }
    }
}

data class MainUiState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
    val shouldShowBottomNav: Boolean = false,
    val hasSession: Boolean = true,
    val mainDestinationList: List<BottomNavBarItem> = emptyList()
): UiState<MainUiState>(isLoading, errorMessage) {
    override fun copyState(isLoading: Boolean, errorMessage: String?): MainUiState =
        copy(isLoading = isLoading, errorMessage = errorMessage)
}

sealed interface MainSideEffects: SideEffect