package com.dreamsoftware.inquize.data.remote.datasource.impl

import com.dreamsoftware.brownie.utils.IBrownieOneSideMapper
import com.dreamsoftware.inquize.data.remote.datasource.IAuthRemoteDataSource
import com.dreamsoftware.inquize.data.remote.dto.AuthUserDTO
import com.dreamsoftware.inquize.data.remote.exception.AuthRemoteDataException
import com.dreamsoftware.inquize.data.remote.exception.SignInRemoteDataException
import com.dreamsoftware.inquize.data.remote.exception.SignUpRemoteDataException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

// Implementation of the IAuthDataSource interface
internal class AuthRemoteDataSourceImpl(
    private val userAuthenticatedMapper: IBrownieOneSideMapper<FirebaseUser, AuthUserDTO>, // Mapper to convert Firebase user to AuthUserDTO
    private val firebaseAuth: FirebaseAuth // Firebase Authentication instance
) : IAuthRemoteDataSource {


    @Throws(AuthRemoteDataException::class)
    override suspend fun getCurrentAuthenticatedUser(): AuthUserDTO = withContext(Dispatchers.IO) {
        try {
            firebaseAuth.currentUser?.let { userAuthenticatedMapper.mapInToOut(it) } ?: throw IllegalStateException("Auth user cannot be null") // Return true if there is a current authenticated user
        } catch (ex: Exception) {
            throw AuthRemoteDataException("An error occurred when trying to check auth state", ex) // Throw AuthException if an error occurs
        }
    }

    /**
     * Gets the UID of the authenticated user.
     * @return the UID of the authenticated user.
     * @throws AuthRemoteDataException if an error occurs or no user is authenticated.
     */
    @Throws(AuthRemoteDataException::class)
    override suspend fun getUserAuthenticatedUid(): String = withContext(Dispatchers.IO) {
        firebaseAuth.currentUser?.uid ?: throw AuthRemoteDataException("An error occurred when trying to check auth state")
        // Return the UID of the current authenticated user or throw AuthException if no user is authenticated
    }

    /**
     * Signs in a user with the given email and password.
     * @param email the email of the user.
     * @param password the password of the user.
     * @return an AuthUserDTO representing the authenticated user.
     * @throws SignInRemoteDataException if an error occurs during the sign-in process.
     */
    @Throws(SignInRemoteDataException::class)
    override suspend fun signIn(email: String, password: String): AuthUserDTO =
        withContext(Dispatchers.IO) {
            try {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .await()?.user?.let { userAuthenticatedMapper.mapInToOut(it) }
                    ?: throw IllegalStateException("Auth user cannot be null")
                // Map the authenticated user to AuthUserDTO or throw an exception if the user is null
            } catch (ex: Exception) {
                throw SignInRemoteDataException("Login failed", ex) // Throw SignInException if an error occurs during sign-in
            }
        }

    /**
     * Signs up a new user with the given email and password.
     * @param email the email of the new user.
     * @param password the password of the new user.
     * @return an AuthUserDTO representing the newly created user.
     * @throws SignUpRemoteDataException if an error occurs during the sign-up process.
     */
    @Throws(SignUpRemoteDataException::class)
    override suspend fun signUp(email: String, password: String): AuthUserDTO =
        withContext(Dispatchers.IO) {
            try {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .await()?.user?.let { userAuthenticatedMapper.mapInToOut(it) }
                    ?: throw IllegalStateException("Auth user cannot be null")
                // Map the newly created user to AuthUserDTO or throw an exception if the user is null
            } catch (ex: Exception) {
                throw SignUpRemoteDataException("Sign up failed", ex) // Throw SignUpException if an error occurs during sign-up
            }
        }

    /**
     * Signs out the currently authenticated user.
     * @throws AuthRemoteDataException if an error occurs during the sign-out process.
     */
    @Throws(AuthRemoteDataException::class)
    override suspend fun closeSession() = withContext(Dispatchers.IO) {
        try {
            firebaseAuth.signOut() // Sign out the current authenticated user
        } catch (ex: Exception) {
            throw AuthRemoteDataException("Logout failed", ex) // Throw AuthException if an error occurs during sign-out
        }
    }
}