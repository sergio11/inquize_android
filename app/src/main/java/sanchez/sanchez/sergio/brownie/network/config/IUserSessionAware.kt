package sanchez.sanchez.sergio.brownie.network.config

interface IUserSessionAware {

    /**
     * Get language for current session
     */
    fun getSessionLanguage(): String

    /**
     * Get token for current session
     */
    fun getSessionToken(): String?

}