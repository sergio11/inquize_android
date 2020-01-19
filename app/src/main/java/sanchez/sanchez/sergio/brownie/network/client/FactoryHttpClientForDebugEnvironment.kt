package sanchez.sanchez.sergio.brownie.network.client

import android.util.Log
import androidx.annotation.RawRes
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import sanchez.sanchez.sergio.brownie.network.interceptors.LoggingInterceptor
import java.io.BufferedInputStream
import java.io.InputStream
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.*
import java.security.cert.CertificateException
import javax.net.ssl.*


/**
 * Essentially, get hold of the default trust manager, create a second trust manager that uses your own trust store.
 * Wrap them both in a custom trust manager implementation that delegates
 * call to both (falling back on the other when one fails).
 */
object FactoryHttpClientForDebugEnvironment: FactoryHttpClient<DefaultClientSettings>() {

    const val TAG = "FAC_HTTP_DEBUG"

    /**
     * Build Http Client
     */
    override fun onBuildHttpClient(httpClientBuild: OkHttpClient.Builder) {

        val trustManagerWrapper = createX509TrustManagerWrapper(
            arrayOf(
                getDefaultX509TrustManager()
            )
        )

        printX509TrustManagerAcceptedIssuers(trustManagerWrapper)

        val sslSocketFactory = createSocketFactory(trustManagerWrapper)
        httpClientBuild.sslSocketFactory(sslSocketFactory, trustManagerWrapper)

    }

    override fun onConfigInterceptors(interceptors: MutableList<Interceptor>) {
        interceptors.add(LoggingInterceptor())
    }

    override fun onConfigNetworkInterceptors(networkInterceptors: MutableList<Interceptor>) {
        networkInterceptors.add(StethoInterceptor())
    }

    /**
     * Private Methods
     */

    private fun createTrustManagerFactoryForCertificate(@RawRes certificateResource: Int): TrustManagerFactory {

        val cf: CertificateFactory = CertificateFactory.getInstance("X.509")
        val caInput: InputStream = BufferedInputStream(settings.appContext.resources.openRawResource(certificateResource))
        val ca: X509Certificate = caInput.use {
            cf.generateCertificate(it) as X509Certificate
        }

        val keyStoreType = KeyStore.getDefaultType()
        val keyStore = KeyStore.getInstance(keyStoreType).apply {
            load(null, null)
            setCertificateEntry("ca", ca)
        }

        return TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).apply {
            init(keyStore)
        }
    }



    /**
     * Get X509 Trust Manager
     */
    private fun getX509TrustManager(tmf: TrustManagerFactory): X509TrustManager {
        val trustManagers = tmf.trustManagers
        check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager))
        { "Unexpected default trust managers:" + Arrays.toString(trustManagers) }
        return trustManagers[0] as X509TrustManager
    }

    private fun printX509TrustManagerAcceptedIssuers(trustManager: X509TrustManager) {
        Log.d(TAG, "AcceptedIssuers -> ${trustManager.acceptedIssuers.size}")
        for(acceptedIssuer in trustManager.acceptedIssuers)
            Log.d(TAG, "subjectDN : ${acceptedIssuer.subjectDN}, issuerDN: ${acceptedIssuer.issuerDN}")
    }

    /**
     * Get Default X509 Trust Manager
     */
    private fun getDefaultX509TrustManager(): X509TrustManager {
        val tmf = TrustManagerFactory
            .getInstance(TrustManagerFactory.getDefaultAlgorithm()).apply {
                // Using null here initialises the TMF with the default trust store.
                init(null as KeyStore?)
            }
        // Get hold of the default trust manager
        return getX509TrustManager(tmf)
    }


    /**
     * Create X509 Trust Manager Wrapper
     * @param trustManagerChain
     */
    private fun createX509TrustManagerWrapper(trustManagerChain: Array<X509TrustManager>): X509TrustManager =
        object : X509TrustManager {

            override fun getAcceptedIssuers(): Array<X509Certificate> =
                trustManagerChain.map { it.acceptedIssuers }.toTypedArray()
                    .flatten().toTypedArray()

            @Throws(CertificateException::class)
            override fun checkServerTrusted(
                chain: Array<X509Certificate>,
                authType: String
            ) {
                for(trustManager in trustManagerChain) {
                    Log.d(TAG, "checkServerTrusted for -> $trustManager")
                    try {
                        trustManager.checkServerTrusted(chain, authType)
                        break
                    } catch (e: CertificateException) {
                        Log.d(TAG, "CertificateException for -> $trustManager")
                    }
                }

            }

            @Throws(CertificateException::class)
            override fun checkClientTrusted(
                chain: Array<X509Certificate>,
                authType: String
            ) {
                for(trustManager in trustManagerChain) {
                    Log.d(TAG, "checkClientTrusted for -> $trustManager")
                    try {
                        trustManager.checkClientTrusted(chain, authType)
                        break
                    } catch (e: CertificateException) {
                        Log.d(TAG, "CertificateException for -> $trustManager")
                    }
                }
            }
        }


    /**
     * Create Socket Factory
     * @param finalTm
     */
    private fun createSocketFactory(finalTm: X509TrustManager): SSLSocketFactory {
        // Create an SSLContext that uses our TrustManager
        val context: SSLContext = SSLContext.getInstance("TLS").apply {
            init(null,  arrayOf<TrustManager>(finalTm), null)
        }
        return context.socketFactory
    }
}