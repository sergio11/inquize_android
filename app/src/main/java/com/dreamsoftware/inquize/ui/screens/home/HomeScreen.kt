package com.dreamsoftware.inquize.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.dreamsoftware.brownie.component.screen.BrownieScreen

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    BrownieScreen(
        viewModel = viewModel,
        onInitialUiState = { HomeUiState() },
        onInit = { loadData() }
    ) { uiState ->
        HomeScreenContent(
            uiState = uiState,
            actionListener = viewModel
        )
    }
}