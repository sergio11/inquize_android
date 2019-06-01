package sanchez.sanchez.sergio.network.exception

import sanchez.sanchez.sergio.network.models.IAPIError
import java.lang.Exception

/**
 * Base Class for handling errors/failures/exceptions.
 */
sealed class Failure: Exception() {

    /**
     * Network Connection
     */
    class NetworkConnection : Failure()

    /**
     * Api Error
     * @param response
     */
    class ApiError(val response: IAPIError?): Failure()

    /**
     * Unauthorized Request Error
     * @param code
     * @param message
     * @param response
     */
    class UnauthorizedRequestError(val code: Int, override val message: String, val response: IAPIError?): Failure()

    /**
     * Bad Request Error
     * @param code
     * @param message
     * @param response
     */
    class BadRequestError(val code: Int, override val message: String, val response: IAPIError?): Failure()


    /**
     * Not Found Error
     * @param code
     * @param message
     * @param response
     */
    class NotFoundError(val code: Int, override val message: String, val response: IAPIError?): Failure()

    /**
     * Unexpected Error
     */
    class UnexpectedError : Failure()


    /** * Extend this class for feature specific failures.*/
    abstract class FeatureFailure: Failure()
}