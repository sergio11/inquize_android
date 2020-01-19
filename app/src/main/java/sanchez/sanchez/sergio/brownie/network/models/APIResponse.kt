package sanchez.sanchez.sergio.brownie.network.models

import com.squareup.moshi.Json

/**
 Generic API Response Wrapper
 **/
data class APIResponse<T> (
    // Response Status
    @field:Json(name = "status") val status: Int,
    // Response Code
    @field:Json(name = "code") val code: String,
    // is response success?
    @field:Json(name = "success") val success: Boolean,
    // Response Data
    @field:Json(name = "data") val data: T
)