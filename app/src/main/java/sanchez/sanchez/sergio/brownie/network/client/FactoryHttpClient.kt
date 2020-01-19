package sanchez.sanchez.sergio.brownie.network.client

import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import sanchez.sanchez.sergio.brownie.network.interceptors.*
import java.io.File


/**
 *  Factory HTTP Client
 */
abstract class FactoryHttpClient<T: IApiClientSettings> {

    /**
     * Client Settings
     */
    protected lateinit var settings: T


    private var httpClient: OkHttpClient? = null

    /**
     * Common Interceptors for both environment
     */
    private val commonInterceptors: MutableList<Interceptor> by lazy {
        mutableListOf(
            AcceptLanguageHeaderInterceptor(settings.sessionAware),
            AuthTokenInterceptor(settings.sessionAware),
            UserAgentInterceptor(settings.userAgent),
            PlatformInterceptor()
            //OfflineInterceptor(settings.appContext)
        )
    }

    /**
     * Common Network interceptors for both environment
     */
    private val commonNetworkInterceptors: MutableList<Interceptor> by lazy {
        mutableListOf<Interceptor>()
    }

    /**
     * Network Cache
     */
    private val commonNetworkCache: Cache by lazy {
        Cache(File(settings.cacheDirLocation), settings.cacheMaxSize)
    }

    operator fun invoke(settings: T): FactoryHttpClient<T> {
        this.settings = settings
        return this
    }

    /**
     * Get OkHttpClient for environment
     */
    fun getHttpClient(): OkHttpClient =
        httpClient ?: buildHttpClient().also {
            httpClient = it
        }


    /**
     * Build Http Client template method
     * @param httpClientBuild
     */
    abstract fun onBuildHttpClient(httpClientBuild: OkHttpClient.Builder)


    /*** Config Interceptors template method
     * @param interceptors current interceptors configured for environment
     */
    open fun onConfigInterceptors(interceptors: MutableList<Interceptor>) {}

    /**
     * Config network Interceptors template method
     * @param networkInterceptors current network interceptors configured for environment
     */
    open fun onConfigNetworkInterceptors(networkInterceptors: MutableList<Interceptor>){}

    private fun buildHttpClient(): OkHttpClient{
        val builder = OkHttpClient.Builder()
        // Configure Network cache
        builder.cache(commonNetworkCache)

        // Custom logic on subclasses
        onBuildHttpClient(builder)

        onConfigInterceptors(commonInterceptors)

        // Add Interceptors
        commonInterceptors.forEach {
            builder.addInterceptor(it)
        }

        onConfigNetworkInterceptors(commonNetworkInterceptors)

        // Add Network interceptors
        commonNetworkInterceptors.forEach {
            builder.addNetworkInterceptor(it)
        }

        return builder.build()
    }

    companion object {
        /**
         * Get Factory for build http client on debug environment
         * @param clientSettings
         */
        @JvmStatic
        fun getFactoryForDebugEnvironment(clientSettings: DefaultClientSettings): FactoryHttpClient<DefaultClientSettings> =
            FactoryHttpClientForDebugEnvironment(clientSettings)

        /**
         * Get Factory for build http client on production environment
         * @param clientSettings
         */
        @JvmStatic
        fun getFactoryForProductionEnvironment(clientSettings: ProductionClientSettings): FactoryHttpClient<ProductionClientSettings> =
            FactoryHttpClientForProductionEnvironment(clientSettings)
    }

}