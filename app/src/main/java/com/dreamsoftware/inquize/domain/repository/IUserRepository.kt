package com.dreamsoftware.inquize.domain.repository

import com.dreamsoftware.inquize.domain.exception.CheckAuthenticatedException
import com.dreamsoftware.inquize.domain.exception.CloseSessionException
import com.dreamsoftware.inquize.domain.exception.SignInException
import com.dreamsoftware.inquize.domain.exception.SignUpException
import com.dreamsoftware.inquize.domain.model.AuthRequestBO
import com.dreamsoftware.inquize.domain.model.AuthUserBO
import com.dreamsoftware.inquize.domain.model.SignUpBO

interface IUserRepository {

    @Throws(CheckAuthenticatedException::class)
    suspend fun getCurrentAuthenticatedUser(): AuthUserBO

    @Throws(CheckAuthenticatedException::class)
    suspend fun getUserAuthenticatedUid(): String

    @Throws(SignInException::class)
    suspend fun signIn(authRequest: AuthRequestBO): AuthUserBO

    @Throws(SignUpException::class)
    suspend fun signUp(data: SignUpBO): AuthUserBO

    @Throws(CloseSessionException::class)
    suspend fun closeSession()
}