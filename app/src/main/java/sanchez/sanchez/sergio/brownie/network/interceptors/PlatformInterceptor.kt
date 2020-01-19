package sanchez.sanchez.sergio.brownie.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class PlatformInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val userAgentRequest = chain.request()
            .newBuilder()
            .header(X_PLATFORM_HEADER, X_PLATFORM_VALUE)
            .build()
        return chain.proceed(userAgentRequest)
    }


    companion object {

        const val X_PLATFORM_HEADER = "X-Platform"
        const val X_PLATFORM_VALUE = "ANDROID"

    }

}