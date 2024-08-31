package com.dreamsoftware.inquize.ui.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.dreamsoftware.inquize.ui.navigation.Screens
import com.dreamsoftware.inquize.ui.screens.home.HomeScreen
import com.dreamsoftware.inquize.ui.screens.settings.SettingsScreen

fun NavGraphBuilder.HomeNavigationGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = Screens.Main.Home.startDestination,
        route = Screens.Main.Home.route
    ) {
        composable(
            route = Screens.Main.Home.Info.route
        ) {
            HomeScreen()
        }

        composable(
            route = Screens.Main.Home.Settings.route
        ) {
            SettingsScreen()
        }
    }
}