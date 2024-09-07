package com.dreamsoftware.inquize.data.repository.impl

import com.dreamsoftware.inquize.data.local.preferences.datasource.IPreferencesDataSource
import com.dreamsoftware.inquize.data.repository.impl.core.SupportRepositoryImpl
import com.dreamsoftware.inquize.domain.exception.PreferenceDataException
import com.dreamsoftware.inquize.domain.repository.IPreferenceRepository
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Preference Repository
 * @param preferenceDataSource
 */
internal class PreferenceRepositoryImpl(
    private val preferenceDataSource: IPreferencesDataSource,
    dispatcher: CoroutineDispatcher
): SupportRepositoryImpl(dispatcher), IPreferenceRepository {

    @Throws(PreferenceDataException::class)
    override suspend fun saveAuthUserUid(uid: String) = safeExecute {
        try {
            preferenceDataSource.saveAuthUserUid(uid)
        } catch (ex: Exception) {
            throw PreferenceDataException("An error occurred when trying to save user uid", ex)
        }
    }

    @Throws(PreferenceDataException::class)
    override suspend fun getAuthUserUid(): String = safeExecute {
        try {
            preferenceDataSource.getAuthUserUid()
        } catch (ex: Exception) {
            throw PreferenceDataException("An error occurred when trying to get user uid", ex)
        }
    }

    @Throws(PreferenceDataException::class)
    override suspend fun setAssistantMutedStatus(isMuted: Boolean): Unit = safeExecute {
        try {
            preferenceDataSource.setAssistantMutedStatus(isMuted)
        } catch (ex: Exception) {
            throw PreferenceDataException("An error occurred when trying to set assistant muted status", ex)
        }
    }

    @Throws(PreferenceDataException::class)
    override suspend fun isAssistantMuted(): Boolean = safeExecute {
        try {
            preferenceDataSource.isAssistantMuted()
        } catch (ex: Exception) {
            throw PreferenceDataException("An error occurred when trying to get assistant muted status", ex)
        }
    }

    @Throws(PreferenceDataException::class)
    override suspend fun clearData() = safeExecute {
        try {
            preferenceDataSource.clearData()
        } catch (ex: Exception) {
            throw PreferenceDataException("An error occurred when trying to clear data", ex)
        }
    }
}