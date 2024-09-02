package com.dreamsoftware.inquize.ui.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.dreamsoftware.inquize.ui.navigation.Screens
import com.dreamsoftware.inquize.ui.screens.chat.ChatScreen
import com.dreamsoftware.inquize.ui.screens.create.CreateInquizeScreen
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
            with(navController) {
                HomeScreen(onGoToChat = {
                    navigate(Screens.Main.Home.Chat.buildRoute(it))
                })
            }
        }

        composable(
            route = Screens.Main.Home.CreateInquize.route
        ) {
            with(navController) {
                CreateInquizeScreen(
                    onGoToChat = {
                        navigate(Screens.Main.Home.Chat.buildRoute(it))
                    },
                    onBackPressed = {
                        popBackStack()
                    }
                )
            }
        }

        composable(
            route = Screens.Main.Home.Chat.route
        ) { navBackStackEntry ->
            navBackStackEntry.arguments?.let { args ->
                Screens.Main.Home.Chat.parseArgs(args)?.let {
                    with(navController) {
                        ChatScreen(
                            args = it,
                            onBackPressed = {
                                popBackStack()
                            }
                        )
                    }
                }
            }
        }

        composable(
            route = Screens.Main.Home.Settings.route
        ) {
            with(navController) {
                SettingsScreen(
                    onBackPressed = {
                        popBackStack()
                    }
                )
            }
        }
    }
}