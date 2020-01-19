package sanchez.sanchez.sergio.brownie.network.repository

import sanchez.sanchez.sergio.brownie.network.models.APIErrorData
import sanchez.sanchez.sergio.brownie.network.models.APIFieldErrorDTO
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import java.io.IOException
import com.squareup.moshi.Types.newParameterizedType
import sanchez.sanchez.sergio.brownie.network.exception.*
import java.lang.Exception


abstract class AbstractNetworkRepository(private val moshi: Moshi) {

    /**
     * Adapter for tranform JSON to API Error model
     */
    private val apiFieldErrorAdapter by lazy {
        val type = newParameterizedType(APIErrorData::class.java, APIFieldErrorDTO::class.java)
        val adapter: JsonAdapter<APIErrorData<APIFieldErrorDTO>> = moshi.adapter(type)
        adapter
    }

    /**
     * Wrap Network Call
     * @param onExecuted
     */
    protected suspend fun <T> wrapNetworkCall(onExecuted: suspend () -> T): T {
        return try {
            onExecuted()
        } catch (exception: Throwable) {
            val retrofitException = RetrofitException.asRetrofitException(exception)
            if (retrofitException.kind === RetrofitException.Kind.NETWORK) {
                throw NetworkErrorException(cause = exception)
            } else {
                try {
                    throw onApiException(retrofitException)
                } catch (e1: IOException) {
                    e1.printStackTrace()
                    throw NetworkErrorException(cause = e1)
                }
            }
        }
    }

    /**
     * On Api Exception
     * @param apiException
     */
    open fun onApiException(apiException: RetrofitException): Exception = apiException.response?.let {
        when(it.code()) {
            BAD_REQUEST_CODE -> {
                val errorData = apiException.transformErrorBodyTo(apiFieldErrorAdapter)
                NetworkBadRequestException(
                    message = it.message(),
                    fields = errorData!!.data
                )
            }
            UNAUTHORIZED_CODE -> NetworkUnauthorizedException(apiException.transformErrorBodyTo(apiFieldErrorAdapter)?.let {error ->
                error.data[0].msg
                } ?: it.message())

            NOT_FOUND_CODE -> NetworkNoResultException(apiException.transformErrorBodyTo(apiFieldErrorAdapter)?.let {error ->
                error.data[0].msg
            } ?: it.message())
            INTERNAL_SERVER_ERROR_CODE -> NetworkErrorException(
                it.message()
            )
            else -> NetworkErrorException()
        }
    } ?: NetworkErrorException()


    companion object {

        const val BAD_REQUEST_CODE: Int = 400
        const val UNAUTHORIZED_CODE: Int = 401
        const val NOT_FOUND_CODE: Int = 404
        const val INTERNAL_SERVER_ERROR_CODE: Int = 500

    }
}