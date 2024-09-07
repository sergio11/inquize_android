package com.dreamsoftware.inquize.ui.screens.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.dreamsoftware.brownie.component.BrownieDialog
import com.dreamsoftware.inquize.R
import com.dreamsoftware.inquize.ui.navigation.graph.RootNavigationGraph

@Composable
fun AppScreenContent(
    navController: NavHostController,
    uiState: AppUiState,
    actionListener: AppScreenActionListener
) {
    with(uiState) {
        BrownieDialog(
            isVisible = !hasNetworkConnectivity,
            mainLogoRes = R.drawable.main_logo,
            titleRes = R.string.generic_lost_network_connectivity_dialog_title,
            descriptionRes = R.string.generic_lost_network_connectivity_dialog_description,
            acceptRes = R.string.generic_lost_network_connectivity_dialog_open_settings_button_text,
            onAcceptClicked = actionListener::onOpenSettings,
        )
        RootNavigationGraph(navController = navController)
    }
}