package sanchez.sanchez.sergio.network.deserializers

import com.squareup.moshi.*
import java.text.SimpleDateFormat
import java.util.*

/**

 Date JSON Adapter

 yyyy/MM/dd HH:mm:s

 **/
class DateJsonAdapter {

    private val dateFormat: String = "yyyy-MM-dd HH:mm:s"

    private val simpleDataFormat: SimpleDateFormat by lazy {
        SimpleDateFormat(dateFormat, Locale.getDefault())
    }

    /**
     * To JSON
     */
    @ToJson fun toJson(date: Date): String =
        simpleDataFormat.format(date)

    /**
     * From JSON
     */
    @FromJson fun fromJson(dateString: String): Date =
        simpleDataFormat
            .parse(dateString)
}