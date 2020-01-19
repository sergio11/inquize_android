package sanchez.sanchez.sergio.brownie.network.exception


open class NetworkException(
    message: String? = null,
    cause: Throwable? = null): Exception(message, cause)