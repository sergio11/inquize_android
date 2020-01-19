package sanchez.sanchez.sergio.brownie.network.exception

/**
 * Network unauthorized Exception
 */
class NetworkUnauthorizedException(
    message: String? = null,
    cause: Throwable? = null): NetworkException(message, cause)