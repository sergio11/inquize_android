package sanchez.sanchez.sergio.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response

/**
    User Agent Interceptor
 **/
class UserAgentInterceptor(private val userAgent: String): Interceptor {


    /** OVERRIDE METHOD **/
    override fun intercept(chain: Interceptor.Chain): Response {
        val userAgentRequest = chain.request()
            .newBuilder()
            .header("User-Agent", userAgent)
            .build()
        return chain.proceed(userAgentRequest)
    }

}