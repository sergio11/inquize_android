package sanchez.sanchez.sergio.brownie.network.factory

import sanchez.sanchez.sergio.brownie.network.adapter.CoroutineCallAdapterFactory
import sanchez.sanchez.sergio.brownie.network.client.IClientSettings
import sanchez.sanchez.sergio.brownie.network.deserializers.DateJsonAdapter
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import sanchez.sanchez.sergio.brownie.network.client.DefaultClientSettings
import sanchez.sanchez.sergio.brownie.network.client.FactoryHttpClient
import sanchez.sanchez.sergio.brownie.network.client.ProductionClientSettings

/**
 *  API Settings
 **/

data class ApiSettings(
    val baseUrl: String,
    val clientSettings: IClientSettings
)

/**
 * Factory Api interface
 */
interface IFactoryAPI {

    /**
     * Get Http Client
     */
    fun getHttpClient(): OkHttpClient

    /**
     * Get Service
     */
    fun <T> getService(klass: Class<T>): T

    /**
     * Clear Cache
     */
    fun clearCache()
}

/**
 * Abstract Factory API
 */
abstract class AbstractFactoryAPI constructor(
    protected val settings: ApiSettings
): IFactoryAPI {


    /**
     * Moshi json Converter
     */
    protected val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(DateJsonAdapter())
            .build()
    }


    /**
     * Retrofit
     */
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl(settings.baseUrl)
            .client(getHttpClient())
            .build()
    }

    /**
     * Get Http Client depends on current environment
     */
    override fun getHttpClient(): OkHttpClient =
        if(settings.clientSettings is ProductionClientSettings)
            FactoryHttpClient.getFactoryForProductionEnvironment(settings.clientSettings as ProductionClientSettings)
                .getHttpClient()
        else
            FactoryHttpClient.getFactoryForDebugEnvironment(settings.clientSettings as DefaultClientSettings)
                .getHttpClient()


    /**
     * Clear Network Cache
     */
    override fun clearCache() {
        getHttpClient().cache()?.evictAll()
    }


    /**
     * Get Service
     */
    override fun <T> getService(klass: Class<T>): T {
        return retrofit.create(klass)
    }

}