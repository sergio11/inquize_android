package com.dreamsoftware.inquize.data.remote.exception

open class RemoteDataException(message: String? = null, cause: Throwable? = null): Exception(message, cause)

// Auth Data Source
class AuthRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)
class SignInRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)
class SignUpRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)

// User Pictures Data Source
class SavePictureRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)
class DeletePictureRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)
class DeleteAllPicturesRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)

// User Questions Data Source
class SaveUserQuestionRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)
class GetUserQuestionByIdRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)
class GetAllUserQuestionsRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)
class DeleteUserQuestionByIdRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)
