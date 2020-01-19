package sanchez.sanchez.sergio.brownie.network.models

import com.squareup.moshi.Json

/**
    API Error
 **/
data class APIErrorData<T>(
    // Response Status
    @field:Json(name = "status") val status: Int,
    // Response Code
    @field:Json(name = "code") val code: String,
    // is response success?
    @field:Json(name = "success") val success: Boolean,
    // oe
    @field:Json(name = "data") val data: List<T>
)

/**
 * API Field Error
 */
data class APIFieldErrorDTO(
    // Location (body)
    @field:Json(name = "location") val location: String? = null,
    // Param activityName
    @field:Json(name = "param") val param: String? = null,
    // Value of param
    @field:Json(name = "value") val value: Any? = null,
    // Error message
    @field:Json(name = "msg") val msg: String
)
