package sanchez.sanchez.sergio.brownie.network.models

import com.squareup.moshi.Json

/**
    API Field
 **/
data class APIFieldMessage(
    // Message
    @field:Json(name = "msg") val msg: String
)