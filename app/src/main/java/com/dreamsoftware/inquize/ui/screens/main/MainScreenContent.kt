package com.dreamsoftware.inquize.ui.screens.main

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import com.dreamsoftware.brownie.component.BrownieBottomBar
import com.dreamsoftware.brownie.component.BrownieSlideDownAnimatedVisibility
import com.dreamsoftware.brownie.component.screen.BrownieScreenContent
import com.dreamsoftware.inquize.ui.navigation.graph.MainNavigationGraph

@Composable
fun MainScreenContent(
    uiState: MainUiState,
    currentDestination: NavDestination?,
    navHostController: NavHostController
) {
    with(uiState) {
        BrownieScreenContent(
            hasTopBar = false,
            enableContentWindowInsets = true,
            drawBottomBarOverContent = true,
            onBuildBottomBar = {
                BrownieSlideDownAnimatedVisibility(
                    visible = shouldShowBottomNav && hasSession
                ) {
                    with(MaterialTheme.colorScheme) {
                        BrownieBottomBar(
                            currentItemRouteSelected = currentDestination?.route,
                            items = mainDestinationList,
                            containerColor = primary,
                            iconColorSelected = primary
                        ) {
                            navHostController.navigate(it.route)
                        }
                    }
                }
            }
        ) {
            MainNavigationGraph(
                navController = navHostController
            )
        }
    }
}