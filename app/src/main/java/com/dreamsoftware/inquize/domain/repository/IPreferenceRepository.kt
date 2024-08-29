package com.dreamsoftware.inquize.domain.repository

import com.dreamsoftware.inquize.domain.exception.PreferenceDataException

interface IPreferenceRepository {

    @Throws(PreferenceDataException::class)
    suspend fun saveAuthUserUid(uid: String)

    @Throws(PreferenceDataException::class)
    suspend fun getAuthUserUid(): String

    @Throws(PreferenceDataException::class)
    suspend fun clearData()
}