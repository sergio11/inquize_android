package com.dreamsoftware.inquize.ui.screens.account.signup

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.dreamsoftware.brownie.component.screen.BrownieScreen

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    onGoToSignIn: () -> Unit,
    onGoToHome: () -> Unit,
    onBackPressed: () -> Unit,
) {
    BrownieScreen(
        viewModel = viewModel,
        onBackPressed = onBackPressed,
        onInitialUiState = { SignUpUiState() },
        onSideEffect = {
            when(it) {
                SignUpSideEffects.NavigateToSignIn -> onGoToSignIn()
                SignUpSideEffects.RegisteredSuccessfully -> onGoToHome()
            }
        }
    ) { uiState ->
        SignUpScreenContent(
            uiState = uiState,
            actionListener = viewModel
        )
    }
}