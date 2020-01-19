package sanchez.sanchez.sergio.brownie.network.deserializers

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
import java.util.*

/**
 Date json Adapter
 yyyy/MM/dd mm:hh:ss
 **/
class DateJsonAdapter {

    private val dateFormat: String = "yyyy-MM-dd hh:mm:ss"

    private val simpleDataFormat: SimpleDateFormat by lazy {
        SimpleDateFormat(dateFormat, Locale.getDefault())
    }

    /**
     * To json
     */
    @ToJson fun toJson(date: Date): String =
        simpleDataFormat.format(date)

    /**
     * From json
     */
    @FromJson fun fromJson(dateString: String): Date? =
        simpleDataFormat
            .parse(dateString)
}