package com.dreamsoftware.inquize.domain.validation

interface ISignUpValidationMessagesResolver {
    fun getInvalidEmailMessage(): String
    fun getShortPasswordMessage(minLength: Int): String
    fun getPasswordsDoNotMatchMessage(): String
}