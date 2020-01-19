package sanchez.sanchez.sergio.brownie.network.client

import okhttp3.*
import java.util.*

/**
 * Factory for create Http Client for production environment
 */
object FactoryHttpClientForProductionEnvironment: FactoryHttpClient<ProductionClientSettings>() {

    /**
     * Cipher Suites for data encryption purposes
     */
    private val cipherSuites: Array<CipherSuite> by lazy {
        arrayOf(CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384,
            CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384,
            CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,
            CipherSuite.TLS_DHE_RSA_WITH_AES_256_GCM_SHA384,
            CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA256,
            CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA,
            CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_256_CBC_SHA,
            CipherSuite.TLS_RSA_WITH_AES_256_GCM_SHA384,
            CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA256,
            CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA,
            CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA)
    }

    /**
     * Build Http Client for production environment
     */
    override fun onBuildHttpClient(httpClientBuild: OkHttpClient.Builder) {
        // Configure Certificate Pinner
        httpClientBuild.certificatePinner(
            CertificatePinner.Builder()
                .add(settings.hostName, *settings.pin.toTypedArray())
                .build())
            .connectionSpecs(
                Arrays.asList(
                    ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .tlsVersions(TlsVersion.TLS_1_3)
                        .cipherSuites(
                            *cipherSuites
                        )
                        .build()))
    }


}
