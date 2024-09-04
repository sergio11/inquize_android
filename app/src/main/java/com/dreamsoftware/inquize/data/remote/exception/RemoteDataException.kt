package com.dreamsoftware.inquize.data.remote.exception

open class RemoteDataException(message: String? = null, cause: Throwable? = null): Exception(message, cause)

// Auth Data Source
class AuthRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)
class SignInRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)
class SignUpRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)

// Images Data Source
class SavePictureRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)
class DeletePictureRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)

// Inquize Data Source
class CreateInquizeRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)
class AddInquizeMessageRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)
class FetchInquizeByIdRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)
class FetchAllInquizeRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)
class DeleteInquizeByIdRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)

// Multimodal LLM Data Source
class ResolveQuestionFromContextRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)
class GenerateImageDescriptionRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)