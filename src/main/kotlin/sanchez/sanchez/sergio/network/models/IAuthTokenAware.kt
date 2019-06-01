package sanchez.sanchez.sergio.network.models

/**
*   Auth Token Aware
 **/
interface IAuthTokenAware {

    /**
     * Get Auth Token
     * @return
     */
    fun getAuthToken(): String?

    /**
     * Set Auth Token
     * @param token
     */
    fun setAuthToken(token: String)
}