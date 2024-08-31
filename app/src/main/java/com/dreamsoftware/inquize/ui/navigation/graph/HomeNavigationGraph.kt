package com.dreamsoftware.inquize.ui.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.dreamsoftware.inquize.ui.navigation.Screens
import com.dreamsoftware.inquize.ui.screens.home.HomeScreen

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
            with(navController) {
                HomeScreen()
            }
        }
    }
}