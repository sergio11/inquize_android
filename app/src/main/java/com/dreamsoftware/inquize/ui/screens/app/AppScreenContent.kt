package com.dreamsoftware.inquize.ui.screens.app

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.dreamsoftware.inquize.ui.navigation.graph.RootNavigationGraph

@Composable
fun AppScreenContent(
    uiState: AppUiState
) {
    RootNavigationGraph(navController = rememberNavController())
}