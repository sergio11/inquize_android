package sanchez.sanchez.sergio.brownie.network.interceptors

import sanchez.sanchez.sergio.brownie.network.config.IUserSessionAware
import okhttp3.Interceptor
import okhttp3.Response


class AcceptLanguageHeaderInterceptor constructor(private val sessionAware: IUserSessionAware): Interceptor {

    /** OVERRIDE METHOD **/
    override fun intercept(chain: Interceptor.Chain): Response {

        val userLanguage = sessionAware.getSessionLanguage()

        val originalRequest = chain.request()
        val requestWithHeaders = originalRequest.newBuilder()
            .header("Accept-Language", userLanguage)
            .build()
        return chain.proceed(requestWithHeaders)
    }

}