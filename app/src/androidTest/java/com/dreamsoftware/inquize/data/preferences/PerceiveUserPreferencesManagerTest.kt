package com.dreamsoftware.inquize.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dreamsoftware.inquize.data.local.preferences.PerceiveUserPreferencesManager
import com.dreamsoftware.inquize.InquizeApplication
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

private const val TEST_DATASTORE_NAME = "test_user_preferences_datastore"

@RunWith(AndroidJUnit4::class)
class PerceiveUserPreferencesManagerTest {

    private lateinit var preferencesDataStore: DataStore<Preferences>
    private lateinit var userPreferencesManager: PerceiveUserPreferencesManager

    @Before
    fun setup() {
        // create context for instrumentation test in android
        val testContext = ApplicationProvider.getApplicationContext<InquizeApplication>()
        preferencesDataStore = PreferenceDataStoreFactory.create {
            testContext.preferencesDataStoreFile(TEST_DATASTORE_NAME)
        }
        userPreferencesManager = PerceiveUserPreferencesManager(preferencesDataStore)
    }

    @After
    fun tearDown() = runTest {
        preferencesDataStore.edit { preferences -> preferences.clear() }
    }

    @Test
    fun isAssistantMutedTest_isPersistentlySetAndUnsetSuccessfully() = runTest {
        // initially the value must be false because not value was set
        assertFalse(userPreferencesManager.preferencesStream.first().isAssistantMuted)

        // set the value to true
        userPreferencesManager.setAssistantMutedStatus(true)
        assertTrue(userPreferencesManager.preferencesStream.first().isAssistantMuted)

        // set the value to false
        userPreferencesManager.setAssistantMutedStatus(false)
        assertFalse(userPreferencesManager.preferencesStream.first().isAssistantMuted)
    }
}