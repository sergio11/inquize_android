package com.dreamsoftware.inquize.data.remote.datasource

import com.dreamsoftware.inquize.data.remote.dto.AuthUserDTO
import com.dreamsoftware.inquize.data.remote.exception.AuthRemoteDataException
import com.dreamsoftware.inquize.data.remote.exception.SignInRemoteDataException
import com.dreamsoftware.inquize.data.remote.exception.SignUpRemoteDataException

/**
 * Interface for authentication data source.
 * Provides methods for user authentication, user retrieval, and session management.
 */
interface IAuthRemoteDataSource {


    @Throws(AuthRemoteDataException::class)
    suspend fun getCurrentAuthenticatedUser(): AuthUserDTO

    /**
     * Gets the UID of the authenticated user.
     * @return the UID of the authenticated user.
     * @throws AuthRemoteDataException if an error occurs or no user is authenticated.
     */
    @Throws(AuthRemoteDataException::class)
    suspend fun getUserAuthenticatedUid(): String

    /**
     * Signs in a user with the given email and password.
     * @param email the email of the user.
     * @param password the password of the user.
     * @return an AuthUserDTO representing the authenticated user.
     * @throws SignInRemoteDataException if an error occurs during the sign-in process.
     */
    @Throws(SignInRemoteDataException::class)
    suspend fun signIn(email: String, password: String): AuthUserDTO

    /**
     * Signs up a new user with the given email and password.
     * @param email the email of the new user.
     * @param password the password of the new user.
     * @return an AuthUserDTO representing the newly created user.
     * @throws SignUpRemoteDataException if an error occurs during the sign-up process.
     */
    @Throws(SignUpRemoteDataException::class)
    suspend fun signUp(email: String, password: String): AuthUserDTO

    /**
     * Signs out the currently authenticated user.
     * @throws AuthRemoteDataException if an error occurs during the sign-out process.
     */
    @Throws(AuthRemoteDataException::class)
    suspend fun closeSession()
}