package sanchez.sanchez.sergio.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import sanchez.sanchez.sergio.network.models.IAuthTokenAware
import java.io.IOException

/**
    Auth Token Interceptor
 **/
class AuthTokenInterceptor
    constructor(private val authTokenAware: IAuthTokenAware): Interceptor {


    /** OVERRIDE METHOD **/

    /**
     * Intercept
     * @param chain
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (!authTokenAware.getAuthToken().isNullOrEmpty())
            request = request.newBuilder()
                .header(TOKEN_HEADER_NAME, authTokenAware.getAuthToken()!!)
                .method(request.method(), request.body())
                .build()
        return chain.proceed(request)
    }

    /** COMPANION OBJETC, CONST ENUM, INNER CLASS **/
    companion object {
        /**
         * Token Header Name
         */
        const val TOKEN_HEADER_NAME = "Authorization"
    }


}