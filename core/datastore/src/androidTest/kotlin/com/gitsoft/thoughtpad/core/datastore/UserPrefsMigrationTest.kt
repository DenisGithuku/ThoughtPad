
/*
* Copyright 2024 Denis Githuku
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* https://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.gitsoft.thoughtpad.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import com.gitsoft.thoughtpad.core.datastore.migrations.Migration_0_1
import com.gitsoft.thoughtpad.core.model.NoteListType
import com.gitsoft.thoughtpad.core.model.ReminderDisplayStyle
import com.gitsoft.thoughtpad.core.model.ReminderFrequency
import com.gitsoft.thoughtpad.core.model.SortOrder
import com.gitsoft.thoughtpad.core.model.ThemeConfig
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before

class UserPreferencesMigrationTest {

    private val context: Context = ApplicationProvider.getApplicationContext()
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var testFile: File

    @Before
    fun setup() {
        testFile = context.preferencesDataStoreFile("test_prefs")

        // This method sets up the data store for testing purposes
        dataStore =
            PreferenceDataStoreFactory.create(
                produceFile = { testFile },
                corruptionHandler = ReplaceFileCorruptionHandler(produceNewData = { emptyPreferences() }),
                migrations = listOf(Migration_0_1) // Include migration
            )
    }

    @After
    fun tearDown() {
        testFile.delete()
    }

    @Test
    fun testMigration() = runBlocking {
        // Step 1: Write old preferences (before migration)
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_CONFIG] = ThemeConfig.LIGHT.name
            preferences[PreferencesKeys.NOTIFICATION_PERMISSION] = true.toString()
        }

        // Step 2: Retrieve the data (after migration) and check default values for new fields
        val preferences = dataStore.data.first()

        // Check the new fields
        assertEquals(
            ReminderDisplayStyle.LIST.name,
            preferences[PreferencesKeys.REMINDER_DISPLAY_STYLE]
        )
        assertEquals(SortOrder.DATE.name, preferences[PreferencesKeys.SORT_ORDER])
        assertFalse(preferences[PreferencesKeys.REMINDER_STATUS].toBoolean())
        assertEquals(ReminderFrequency.WEEKLY.name, preferences[PreferencesKeys.REMINDER_FREQUENCY])
        assertEquals(NoteListType.GRID.name, preferences[PreferencesKeys.NOTE_LIST_TYPE])

        // Optional: Check that existing preferences are correctly preserved
        assertEquals(ThemeConfig.LIGHT.name, preferences[PreferencesKeys.THEME_CONFIG])
        assertTrue(preferences[PreferencesKeys.NOTIFICATION_PERMISSION].toBoolean())
    }
}
