package com.dreamsoftware.inquize.data.repository.impl

import com.dreamsoftware.brownie.utils.IBrownieOneSideMapper
import com.dreamsoftware.inquize.data.remote.datasource.IAuthRemoteDataSource
import com.dreamsoftware.inquize.data.remote.dto.AuthUserDTO
import com.dreamsoftware.inquize.data.repository.impl.core.SupportRepositoryImpl
import com.dreamsoftware.inquize.domain.exception.CheckAuthenticatedException
import com.dreamsoftware.inquize.domain.exception.CloseSessionException
import com.dreamsoftware.inquize.domain.exception.SignInException
import com.dreamsoftware.inquize.domain.exception.SignUpException
import com.dreamsoftware.inquize.domain.model.AuthRequestBO
import com.dreamsoftware.inquize.domain.model.AuthUserBO
import com.dreamsoftware.inquize.domain.model.SignUpBO
import com.dreamsoftware.inquize.domain.repository.IUserRepository
import kotlinx.coroutines.CoroutineDispatcher

internal class UserRepositoryImpl(
    private val authDataSource: IAuthRemoteDataSource,
    private val authUserMapper: IBrownieOneSideMapper<AuthUserDTO, AuthUserBO>,
    dispatcher: CoroutineDispatcher
): SupportRepositoryImpl(dispatcher), IUserRepository {

    @Throws(CheckAuthenticatedException::class)
    override suspend fun getCurrentAuthenticatedUser(): AuthUserBO = safeExecute {
        try {
            val authUserDTO = authDataSource.getCurrentAuthenticatedUser()
            authUserMapper.mapInToOut(authUserDTO)
        } catch (ex: Exception) {
            throw CheckAuthenticatedException(
                "An error occurred when trying to check if user is already authenticated",
                ex
            )
        }
    }

    @Throws(CheckAuthenticatedException::class)
    override suspend fun getUserAuthenticatedUid(): String = safeExecute {
        try {
            authDataSource.getUserAuthenticatedUid()
        } catch (ex: Exception) {
            throw CheckAuthenticatedException(
                "An error occurred when trying to check if user is already authenticated",
                ex
            )
        }
    }

    @Throws(SignInException::class)
    override suspend fun signIn(authRequest: AuthRequestBO): AuthUserBO = safeExecute {
        try {
            with(authRequest) {
                val authUserDTO = authDataSource.signIn(email, password)
                authUserMapper.mapInToOut(authUserDTO)
            }
        } catch (ex: Exception) {
            throw SignInException("An error occurred when trying to sign in user", ex)
        }
    }

    @Throws(SignUpException::class)
    override suspend fun signUp(data: SignUpBO): AuthUserBO = safeExecute {
        try {
            with(data) {
                val authUserDTO = authDataSource.signUp(
                    email = email,
                    password = password
                )
                authUserMapper.mapInToOut(authUserDTO)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            throw SignUpException("An error occurred when trying to sign up user", ex)
        }
    }

    @Throws(CloseSessionException::class)
    override suspend fun closeSession() = safeExecute {
        try {
            authDataSource.closeSession()
        } catch (ex: Exception) {
            throw CloseSessionException("An error occurred when trying to close user session", ex)
        }
    }
}