package sanchez.sanchez.sergio.brownie.network.exception

import sanchez.sanchez.sergio.brownie.network.models.APIFieldErrorDTO

class NetworkBadRequestException(
    message: String? = null,
    val fields: List<APIFieldErrorDTO>,
    cause: Throwable? = null): NetworkException(message, cause)