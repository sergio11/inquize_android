package com.dreamsoftware.inquize.ui.screens.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dreamsoftware.brownie.component.screen.BrownieScreen

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    with(navController) {
        val navBackStackEntry by currentBackStackEntryAsState()
        val hideBottomItems by remember {
            derivedStateOf {
                when (navBackStackEntry?.destination?.route) {
                    else -> true
                }
            }
        }
        LaunchedEffect(hideBottomItems) {
            viewModel.onBottomItemsVisibilityChanged(hideBottomItems)
        }
        BrownieScreen(
            viewModel = viewModel,
            onInitialUiState = { MainUiState() }
        ) { uiState ->
            MainScreenContent(
                uiState = uiState,
                currentDestination = navBackStackEntry?.destination,
                navHostController = navController
            )
        }
    }
}