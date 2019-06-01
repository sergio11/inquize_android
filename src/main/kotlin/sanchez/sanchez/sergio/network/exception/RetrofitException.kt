package sanchez.sanchez.sergio.network.exception

import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException

/**
 * Retrofit Exception
 */
class RetrofitException(
        private val retrofit: Retrofit,
        message: String,
        /** The request URL which produced the response.  */
                        val url: String? = null,
        /** Response object containing status code, headers, body, etc.  */
                        val response: Response<*>? = null,
        /** The event kind which triggered this response.  */
                        val kind: Kind,
        exception: Throwable? = null) : RuntimeException(message, exception) {


    /**
     * HTTP response body converted to specified `type`. `null` if there is no
     * response.
     *
     * @throws IOException if unable to convert the body to the specified `type`.
     */
    @Throws(IOException::class)
    fun <T> getErrorBodyAs(type: Class<T>): T? {
        if (response?.errorBody() == null) {
            return null
        }
        val converter = retrofit.responseBodyConverter<T>(type, arrayOfNulls(0))
        return converter.convert(response.errorBody()!!)
    }


    /** COMPANION OBJETC, CONST ENUM, INNER CLASS **/

    companion object {

        private const val serialVersionUID = 1L

        fun httpError(retrofit: Retrofit, url: String, response: Response<*>): RetrofitException {
            val message = response.code().toString() + " " + response.message()
            return RetrofitException(retrofit, message, url, response, Kind.HTTP)
        }

        fun networkError(retrofit: Retrofit, exception: IOException): RetrofitException {
            return RetrofitException(
                retrofit = retrofit,
                message = exception.message!!,
                kind = Kind.NETWORK)
        }

        fun unexpectedError(retrofit: Retrofit, exception: Throwable): RetrofitException {
            return RetrofitException(
                retrofit = retrofit,
                message = exception.message!!,
                kind = Kind.UNEXPECTED,
                exception = exception)
        }
    }


    /** Identifies the event kind which triggered a [RetrofitException].  */
    enum class Kind {
        /** An [IOException] occurred while communicating to the server.  */
        NETWORK,
        /** A non-200 HTTP status code was received from the server.  */
        HTTP,
        /**
         * An internal response occurred while attempting to wrapNetworkCall a request. It is best practice to
         * re-throw this exception so your application crashes.
         */
        UNEXPECTED
    }

}