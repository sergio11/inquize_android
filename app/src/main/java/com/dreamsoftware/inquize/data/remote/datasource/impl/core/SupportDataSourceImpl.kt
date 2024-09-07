package com.dreamsoftware.inquize.data.remote.datasource.impl.core

import com.dreamsoftware.inquize.data.remote.exception.RemoteDataException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal abstract class SupportDataSourceImpl(
    private val dispatcher: CoroutineDispatcher
) {
    protected suspend fun <T> safeExecution(
        onExecuted: suspend () -> T,
        onErrorOccurred: ((ex: Exception) -> RemoteDataException)? = null
    ): T = withContext(dispatcher) {
        try {
            onExecuted()
        } catch (ex: Exception) {
            throw onErrorOccurred?.invoke(ex) ?: RemoteDataException("An error occurred while executing the operation")
        }
    }
}