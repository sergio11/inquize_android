package com.dreamsoftware.inquize.ui.navigation

import android.os.Bundle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.dreamsoftware.inquize.ui.screens.chat.ChatScreenArgs

sealed class Screens(val route: String, arguments: List<NamedNavArgument> = emptyList()) {

    data object Splash: Screens("splash")
    data object Onboarding: Screens("onboarding")
    data object SignIn: Screens("sign_in")
    data object SignUp: Screens("sign_up")


    sealed class Main private constructor(route: String) : Screens(route) {
        companion object {
            const val route = "main"
            const val startDestination = Home.route
        }

        sealed class Home private constructor(route: String) : Screens(route) {
            companion object {
                const val route = "home"
                val startDestination = Info.route
            }
            data object Info : Screens("info")
            data object CreateInquize : Screens("CreateInquize")
            data object Settings: Screens("settings")
            data object Chat : Screens("chat/{inquize_id}", arguments = listOf(
                navArgument("inquize_id") {
                    type = NavType.StringType
                }
            )) {
                fun buildRoute(inquizeId: String): String =
                    route.replace(
                        oldValue = "{inquize_id}",
                        newValue = inquizeId
                    )

                fun parseArgs(args: Bundle): ChatScreenArgs? = with(args) {
                    getString("inquize_id")?.let {
                        ChatScreenArgs(id = it)
                    }
                }
            }
        }
    }
}