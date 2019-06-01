package sanchez.sanchez.sergio.network

import sanchez.sanchez.sergio.network.adapter.CoroutineCallAdapterFactory
import sanchez.sanchez.sergio.network.deserializers.DateJsonAdapter
import com.squareup.moshi.Moshi
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


/**
 * Factory Network
 **/
object FactoryNetwork {

    /**
     * API Settings
     */
    private lateinit var settings: ApiSettings

    /**
     * Moshi JSON Converter
     */
    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(DateJsonAdapter())
            .build()
    }

    /**
     * Http Client
     */
    private val httpClient: OkHttpClient by lazy {
        IFactoryHttpClient.getFactoryForConfig(settings.clientSettings)
            .getHttpClient()
    }

    /**
     * Retrofit
     */
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl(settings.baseUrl)
            .client(httpClient)
            .build()
    }

    /**
     *
     */
    operator fun invoke(settings: ApiSettings): FactoryNetwork {
        FactoryNetwork.settings = settings
        return this
    }


    /**
     * Get Service
     */
    private fun <T> getService(klass: Class<T>): T {
        return retrofit.create(klass)
    }
}


