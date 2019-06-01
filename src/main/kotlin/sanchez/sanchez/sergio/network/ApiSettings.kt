package sanchez.sanchez.sergio.network

import sanchez.sanchez.sergio.network.models.IAuthTokenAware


/**

 API Settings

 **/

data class ApiSettings(
    val baseUrl: String,
    val clientSettings: IClientSettings
)


/**

Http Client Configuration

 **/

interface IClientSettings

// Settings for debug http client
data class DebugClientSettings(
    val userAgent: String,
    val userLanguage: String? = null,
    val authTokenAware: IAuthTokenAware
): IClientSettings

// Settings for production http client
data class ProductionClientSettings(
    val userAgent: String,
    val userLanguage: String? = null,
    val hostName: String,
    val pin: List<String>,
    val authTokenAware: IAuthTokenAware
): IClientSettings