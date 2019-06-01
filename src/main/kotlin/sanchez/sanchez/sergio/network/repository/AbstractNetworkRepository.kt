package sanchez.sanchez.sergio.network.repository


import retrofit2.HttpException
import retrofit2.Retrofit
import sanchez.sanchez.sergio.network.exception.Failure
import sanchez.sanchez.sergio.network.exception.RetrofitException
import sanchez.sanchez.sergio.network.models.IAPIError
import java.io.IOException

private const val UNAUTHORIZED_REQUEST_ERROR = 401
private const val BAD_REQUEST_ERROR = 400
private const val NOT_FOUND_ERROR = 404


/**
 Abstract Repository
 **/
abstract class AbstractNetworkRepository(private val retrofit: Retrofit) {

    /**
     * Wrap Network Call
     * @param onExecuted
     */
    protected suspend fun <T> wrapNetworkCall(onExecuted: suspend () -> T): T {
        return try {
            onExecuted()
        } catch (exception: Throwable) {
            val retrofitException = asRetrofitException(exception)
            if (retrofitException.kind === RetrofitException.Kind.NETWORK) {
                throw Failure.NetworkConnection()
            } else {
                try {
                   // val response = retrofitException.getErrorBodyAs(APIError::class.java)
                    throw onApiException(retrofitException, null)
                } catch (e1: IOException) {
                    e1.printStackTrace()
                    throw Failure.UnexpectedError()
                }
            }
        }
    }

    /**
     * On Api Exception
     * @param apiException
     * @param response
     */
    open fun onApiException(apiException: RetrofitException, response: IAPIError?): Failure = apiException.response?.let {
        when(it.code()) {
            UNAUTHORIZED_REQUEST_ERROR -> Failure.UnauthorizedRequestError(it.code(), it.message(), response)
            BAD_REQUEST_ERROR -> Failure.BadRequestError(it.code(), it.message(), response)
            NOT_FOUND_ERROR -> Failure.NotFoundError(it.code(), it.message(), response)
            else -> Failure.ApiError(response)
        }
    } ?: Failure.ApiError(response)


    /**
     * As Retrofit Exception
     * @param throwable
     * @return
     */
   private  fun asRetrofitException(throwable: Throwable): RetrofitException {
        // We had non-200 http response
        if (throwable is HttpException) {
            val response = throwable.response()
            return RetrofitException.httpError(retrofit, response.raw().request().url().toString(),
                response)
        }
        // A network response happened
        return if (throwable is IOException) {
            RetrofitException.networkError(retrofit, throwable)
        } else RetrofitException.unexpectedError(retrofit, throwable)

    }
}