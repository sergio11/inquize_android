package com.dreamsoftware.inquize.data.remote.exception

open class RemoteDataException(message: String? = null, cause: Throwable? = null): Exception(message, cause)

// Auth Data Source
class AuthRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)
class SignInRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)
class SignUpRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)