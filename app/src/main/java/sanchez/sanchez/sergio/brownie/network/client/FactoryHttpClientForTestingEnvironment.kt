package sanchez.sanchez.sergio.brownie.network.client

import sanchez.sanchez.sergio.brownie.network.interceptors.LoggingInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


/**
 * Factory for create Http Client for testing environment
 */
object FactoryHttpClientForTestingEnvironment: FactoryHttpClient<DefaultClientSettings>() {


    override fun onBuildHttpClient(httpClientBuild: OkHttpClient.Builder) {
        httpClientBuild
            .connectTimeout(2, TimeUnit.SECONDS) // For testing purposes
            .readTimeout(2, TimeUnit.SECONDS) // For testing purposes
            .writeTimeout(2, TimeUnit.SECONDS)
    }

    override fun onConfigInterceptors(interceptors: MutableList<Interceptor>) {
        interceptors.plus(LoggingInterceptor())
    }
}