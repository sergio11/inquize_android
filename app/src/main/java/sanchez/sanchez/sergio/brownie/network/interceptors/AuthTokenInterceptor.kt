package sanchez.sanchez.sergio.brownie.network.interceptors

import sanchez.sanchez.sergio.brownie.network.config.IUserSessionAware
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
    Auth Token Interceptor
 **/
class AuthTokenInterceptor
    constructor(private val sessionAware: IUserSessionAware): Interceptor {


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
        if (!sessionAware.getSessionToken().isNullOrEmpty())
            request = request.newBuilder()
                .header(TOKEN_HEADER_NAME, sessionAware.getSessionToken()!!)
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