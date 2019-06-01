package sanchez.sanchez.sergio.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import java.util.*

/**
    Accept Language Header Interceptor
 **/
class AcceptLanguageHeaderInterceptor(private val userLanguage: String?): Interceptor {

    /** ATTRIBUTES **/
    private val defaultUserLanguage: String by lazy {
        Locale.getDefault().language
    }

    /** OVERRIDE METHOD **/
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestWithHeaders = originalRequest.newBuilder()
            .header("Accept-Language", userLanguage ?: defaultUserLanguage)
            .build()
        return chain.proceed(requestWithHeaders)
    }

}