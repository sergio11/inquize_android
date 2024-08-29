package com.dreamsoftware.inquize.ui.screens.account.signin

import com.dreamsoftware.brownie.core.IBrownieScreenActionListener

interface SignInScreenActionListener: IBrownieScreenActionListener {
    fun onEmailChanged(newEmail: String)
    fun onPasswordChanged(newPassword: String)
    fun onSignIn()
}