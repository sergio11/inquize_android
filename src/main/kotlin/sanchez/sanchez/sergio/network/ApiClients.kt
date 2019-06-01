package sanchez.sanchez.sergio.network

import okhttp3.*
import sanchez.sanchez.sergio.network.interceptors.AcceptLanguageHeaderInterceptor
import sanchez.sanchez.sergio.network.interceptors.AuthTokenInterceptor
import sanchez.sanchez.sergio.network.interceptors.UserAgentInterceptor
import java.lang.IllegalArgumentException
import java.util.*
import kotlin.collections.ArrayList

/**
 *
 *  HTTP Client
 *
 */
interface IFactoryHttpClient {

    /**
     * Get OkHttpClient for environment
     */
    fun getHttpClient(): OkHttpClient


    companion object {

        /**
         * Get Client Factory for config
         */
        fun getFactoryForConfig(clientSettings: IClientSettings): IFactoryHttpClient =
                when(clientSettings) {
                    is ProductionClientSettings -> FactoryHttpClientForProductionEnvironment(
                            clientSettings
                    )
                    is DebugClientSettings -> FactoryHttpClientForDebugEnvironment(
                            clientSettings
                    )

                    else -> throw IllegalArgumentException("Client Settings is not valid")
                }

    }

}

/**
 * Factory for create Http Client for production environment
 */
object FactoryHttpClientForProductionEnvironment: IFactoryHttpClient {

    // Client Settings
    private lateinit var settings: ProductionClientSettings

    // Interceptors
    private val interceptors: List<Interceptor> by lazy {
        listOf(
                AcceptLanguageHeaderInterceptor(settings.userLanguage),
                AuthTokenInterceptor(settings.authTokenAware),
                UserAgentInterceptor(settings.userAgent)
        )
    }

    // Network Interceptors
    private val networkInterceptors: List<Interceptor> by lazy {
        ArrayList<Interceptor>()
    }

    /**
     *
     */
    operator fun invoke(settings: ProductionClientSettings): FactoryHttpClientForProductionEnvironment {
        FactoryHttpClientForProductionEnvironment.settings = settings
        return this
    }

    /**
     * Get Http Client For Production
     */
    override fun getHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()

        // Configure Certificate Pinner
        builder.certificatePinner(
                CertificatePinner.Builder()
                        .add(settings.hostName, *settings.pin.toTypedArray())
                        .build())
                .connectionSpecs(Arrays.asList(ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .tlsVersions(TlsVersion.TLS_1_2)
                        .cipherSuites(
                                CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384,
                                CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384,
                                CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,
                                CipherSuite.TLS_DHE_RSA_WITH_AES_256_GCM_SHA384,
                                CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA256,
                                CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA,
                                CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_256_CBC_SHA,
                                CipherSuite.TLS_RSA_WITH_AES_256_GCM_SHA384,
                                CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA256,
                                CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA,
                                CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA
                        )
                        .build()))

        // Add Interceptors
        interceptors.forEach {
            builder.addInterceptor(it)
        }

        // Add Network interceptors
        networkInterceptors.forEach {
            builder.addNetworkInterceptor(it)
        }

        return builder.build()
    }
}


/**
 * Factory for create Http Client for debug environment
 */
object FactoryHttpClientForDebugEnvironment: IFactoryHttpClient {

    // Client Settings
    private lateinit var settings: DebugClientSettings

    // Interceptors
    private val interceptors: List<Interceptor> by lazy {
        listOf(
                AcceptLanguageHeaderInterceptor(settings.userLanguage),
                AuthTokenInterceptor(settings.authTokenAware),
                UserAgentInterceptor(settings.userAgent)
        )
    }

    // Network Interceptors
    private val networkInterceptors: List<Interceptor> by lazy {
        ArrayList<Interceptor>()
    }

    /**
     * Create Http Client
     */
    override fun getHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        interceptors.forEach { builder.addInterceptor(it) }
        networkInterceptors.forEach { builder.addNetworkInterceptor(it) }
        return builder.build()
    }

    /**
     *
     */
    operator fun invoke(settings: DebugClientSettings): FactoryHttpClientForDebugEnvironment {
        FactoryHttpClientForDebugEnvironment.settings = settings
        return this
    }

}