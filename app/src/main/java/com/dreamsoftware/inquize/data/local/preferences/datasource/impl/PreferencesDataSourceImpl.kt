package com.dreamsoftware.inquize.data.local.preferences.datasource.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.dreamsoftware.inquize.data.local.preferences.datasource.IPreferencesDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException

internal class PreferencesDataSourceImpl(
    private val dataStore: DataStore<Preferences>,
    private val dispatcher: CoroutineDispatcher
): IPreferencesDataSource {

    private object DatastoreKeys {
        val AUTH_USER_UID_KEY = stringPreferencesKey("auth_user_uid_key")
        val ASSISTANT_MUTED_KEY = booleanPreferencesKey("assistant_muted_key")
    }

    private val dataStoreFlow: Flow<Preferences>
        get() = dataStore.data.catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }

    override suspend fun saveAuthUserUid(uid: String): Unit = withContext(dispatcher) {
        dataStore.edit {
            it[DatastoreKeys.AUTH_USER_UID_KEY] = uid
        }
    }

    override suspend fun getAuthUserUid(): String = withContext(dispatcher) {
        dataStoreFlow.map {
            it[DatastoreKeys.AUTH_USER_UID_KEY].orEmpty()
        }.first()
    }

    override suspend fun setAssistantMutedStatus(isMuted: Boolean): Unit = withContext(dispatcher) {
        dataStore.edit {
            it[DatastoreKeys.ASSISTANT_MUTED_KEY] = isMuted
        }
    }

    override suspend fun isAssistantMuted(): Boolean = withContext(dispatcher) {
        dataStoreFlow.map {
            it[DatastoreKeys.ASSISTANT_MUTED_KEY] ?: true
        }.first()
    }

    override suspend fun clearData() {
        dataStore.edit {
            it.clear()
        }
    }
}