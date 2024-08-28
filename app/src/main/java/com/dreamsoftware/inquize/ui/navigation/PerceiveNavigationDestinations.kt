package com.dreamsoftware.inquize.ui.navigation

import android.net.Uri
import java.util.Base64

sealed class PerceiveNavigationDestinations(val route: String) {
    data object WelcomeScreen : PerceiveNavigationDestinations("welcome_screen")
    data object HomeScreen : PerceiveNavigationDestinations("home_screen")

    data object ChatScreen :
        PerceiveNavigationDestinations("chat_screen/{initial_user_prompt}/{associated_image_uri_base64}") {
        const val NAV_ARG_INITIAL_USER_PROMPT = "initial_user_prompt"
        const val NAV_ARG_ASSOCIATED_IMAGE_URI_BASE64 = "associated_image_uri_base64"
        fun buildRoute(initialUserPrompt: String, associatedImageUri: Uri): String {
            val imageUriByteArray = associatedImageUri.toString().toByteArray()
            val encodedImageUri = Base64.getEncoder().encodeToString(imageUriByteArray)
            return "chat_screen/$initialUserPrompt/$encodedImageUri"
        }
    }
}