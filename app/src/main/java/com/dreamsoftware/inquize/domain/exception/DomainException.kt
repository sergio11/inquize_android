package com.dreamsoftware.inquize.domain.exception

open class DomainRepositoryException(message: String? = null, cause: Throwable? = null): Exception(message, cause)
class RepositoryOperationException(message: String? = null, cause: Throwable? = null) : DomainRepositoryException(message, cause)

class PreferenceDataException(message: String? = null, cause: Throwable? = null): DomainRepositoryException(message, cause)
class InvalidDataException(errors: Map<String, String>, message: String? = null, cause: Throwable? = null): DomainRepositoryException(message, cause)

abstract class UserDataException(message: String? = null, cause: Throwable? = null): DomainRepositoryException(message, cause)
class CheckAuthenticatedException(message: String? = null, cause: Throwable? = null): UserDataException(message, cause)
class SignInException(message: String? = null, cause: Throwable? = null): UserDataException(message, cause)
class SignUpException(message: String? = null, cause: Throwable? = null): UserDataException(message, cause)
class CloseSessionException(message: String? = null, cause: Throwable? = null): UserDataException(message, cause)

class SearchInquizeException(message: String? = null, cause: Throwable? = null): DomainRepositoryException(message, cause)
class AddInquizeMessageException(message: String? = null, cause: Throwable? = null): DomainRepositoryException(message, cause)
class SaveInquizeException(message: String? = null, cause: Throwable? = null): DomainRepositoryException(message, cause)
class FetchInquizeByIdException(message: String? = null, cause: Throwable? = null): DomainRepositoryException(message, cause)
class FetchAllInquizeException(message: String? = null, cause: Throwable? = null): DomainRepositoryException(message, cause)
class DeleteInquizeByIdException(message: String? = null, cause: Throwable? = null): DomainRepositoryException(message, cause)

class SavePictureException(message: String? = null, cause: Throwable? = null): DomainRepositoryException(message, cause)
class DeletePictureException(message: String? = null, cause: Throwable? = null): DomainRepositoryException(message, cause)

// Multimodal LLM
class ResolveQuestionFromContextException(message: String? = null, cause: Throwable? = null): DomainRepositoryException(message, cause)
class GenerateImageDescriptionException(message: String? = null, cause: Throwable? = null): DomainRepositoryException(message, cause)