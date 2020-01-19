package sanchez.sanchez.sergio.brownie.network.client

import android.content.Context
import sanchez.sanchez.sergio.brownie.network.config.IUserSessionAware

/**
 *  Http Client Configuration
 **/

const val DEFAULT_CACHE_SIZE: Long =  10 * 1024 * 1024 // 10 MB

/**
 * Common Http Client Settings
 */
interface IClientSettings {
    val appContext: Context
}

interface IApiClientSettings: IClientSettings {
    val userAgent: String
    val sessionAware: IUserSessionAware
    val cacheMaxSize: Long
    val cacheDirLocation: String
}

/**
 * Settings for debug http client
 */
data class DefaultClientSettings @JvmOverloads constructor(
    override val appContext: Context,
    override val userAgent: String,
    override val sessionAware: IUserSessionAware,
    override val cacheMaxSize: Long = DEFAULT_CACHE_SIZE,
    override  val cacheDirLocation: String
): IApiClientSettings

/**
 * Settings for production http client
 */
data class ProductionClientSettings @JvmOverloads constructor(
    override val appContext: Context,
    override val userAgent: String,
    override val sessionAware: IUserSessionAware,
    override val cacheMaxSize: Long = DEFAULT_CACHE_SIZE,
    override val cacheDirLocation: String,
    val hostName: String,
    val pin: List<String>
): IApiClientSettings
