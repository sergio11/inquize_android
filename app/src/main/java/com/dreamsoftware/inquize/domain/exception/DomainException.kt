package com.dreamsoftware.inquize.domain.exception

import com.dreamsoftware.inquize.data.remote.exception.RemoteDataException

open class DomainRepositoryException(message: String? = null, cause: Throwable? = null): Exception(message, cause)
class RepositoryOperationException(message: String? = null, cause: Throwable? = null) : DomainRepositoryException(message, cause)

class PreferenceDataException(message: String? = null, cause: Throwable? = null): DomainRepositoryException(message, cause)
class InvalidDataException(errors: Map<String, String>, message: String? = null, cause: Throwable? = null): DomainRepositoryException(message, cause)

abstract class UserDataException(message: String? = null, cause: Throwable? = null): DomainRepositoryException(message, cause)
class CheckAuthenticatedException(message: String? = null, cause: Throwable? = null): UserDataException(message, cause)
class SignInException(message: String? = null, cause: Throwable? = null): UserDataException(message, cause)
class SignUpException(message: String? = null, cause: Throwable? = null): UserDataException(message, cause)
class CloseSessionException(message: String? = null, cause: Throwable? = null): UserDataException(message, cause)

class SaveInquizeException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)
class FetchInquizeByIdException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)
class FetchAllInquizeException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)
class DeleteInquizeByIdException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)