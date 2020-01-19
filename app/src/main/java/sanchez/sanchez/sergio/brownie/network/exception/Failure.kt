package sanchez.sanchez.sergio.brownie.network.exception

import sanchez.sanchez.sergio.brownie.network.models.APIErrorData
import sanchez.sanchez.sergio.brownie.network.models.APIFieldErrorDTO

interface ApiError<T> {
    val code: Int
    val message: String
    val data: APIErrorData<T>?
}


/**
 * Common Api Error
 * @param code
 * @param message
 * @param data
 */
class CommonApiError(
    override val code: Int,
    override val message: String,
    override val data: APIErrorData<APIFieldErrorDTO>?): ApiError<APIFieldErrorDTO>
