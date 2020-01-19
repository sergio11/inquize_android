package sanchez.sanchez.sergio.brownie.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response

/**
 * User Agent Interceptor
 **/
class UserAgentInterceptor(private val userAgent: String): Interceptor {


    /** OVERRIDE METHOD **/
    override fun intercept(chain: Interceptor.Chain): Response {
        val userAgentRequest = chain.request()
            .newBuilder()
            .header(USER_AGENT_HEADER, userAgent)
            .build()
        return chain.proceed(userAgentRequest)
    }

    companion object {

        const val USER_AGENT_HEADER = "User-Agent"
    }

}