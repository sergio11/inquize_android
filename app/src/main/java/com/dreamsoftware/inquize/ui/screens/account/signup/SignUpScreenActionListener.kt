package com.dreamsoftware.inquize.ui.screens.account.signup

import com.dreamsoftware.brownie.core.IBrownieScreenActionListener

interface SignUpScreenActionListener: IBrownieScreenActionListener {
    fun onEmailChanged(newEmail: String)
    fun onPasswordChanged(newPassword: String)
    fun onConfirmPasswordChanged(newConfirmPassword: String)
    fun onSignUp()
    fun onNavigateToSignIn()
}