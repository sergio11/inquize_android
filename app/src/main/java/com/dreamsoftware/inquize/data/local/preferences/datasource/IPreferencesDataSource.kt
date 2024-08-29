package com.dreamsoftware.inquize.data.local.preferences.datasource

interface IPreferencesDataSource {
    suspend fun saveAuthUserUid(uid: String)

    suspend fun getAuthUserUid(): String

    suspend fun clearData()
}