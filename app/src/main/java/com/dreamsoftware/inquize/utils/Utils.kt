package com.dreamsoftware.inquize.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import kotlin.enums.enumEntries

@OptIn(ExperimentalStdlibApi::class)
inline fun <reified T : Enum<T>> enumNameOfOrDefault(name: String, default: T): T =
    enumEntries<T>().find { it.name == name } ?: default

@OptIn(ExperimentalStdlibApi::class)
inline fun <reified T : Enum<T>> enumOfOrDefault(predicate: (T) -> Boolean, default: T): T =
    enumEntries<T>().find(predicate) ?: default

inline fun <T1 : Any, T2 : Any, R> combinedLet(value1: T1?, value2: T2?, block: (T1, T2) -> R): R? =
    if (value1 != null && value2 != null) {
        block(value1, value2)
    } else {
        null
    }


suspend fun String.urlToBitmap(dispatcher: CoroutineDispatcher): Bitmap? = withContext(dispatcher) {
    try {
        (URL(this@urlToBitmap).openConnection() as? HttpURLConnection)?.run {
            requestMethod = "GET"
            connect()
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream.use { BitmapFactory.decodeStream(it) }
            } else {
                null
            }
        } ?: run {
            null
        }
    } catch (e: IOException) {
        null
    }
}