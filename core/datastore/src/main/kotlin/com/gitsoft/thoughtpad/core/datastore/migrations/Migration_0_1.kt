
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
package com.gitsoft.thoughtpad.core.datastore.migrations

import androidx.datastore.core.DataMigration
import androidx.datastore.preferences.core.Preferences
import com.gitsoft.thoughtpad.core.datastore.PreferencesKeys
import com.gitsoft.thoughtpad.core.model.NoteListType
import com.gitsoft.thoughtpad.core.model.ReminderDisplayStyle
import com.gitsoft.thoughtpad.core.model.ReminderFrequency
import com.gitsoft.thoughtpad.core.model.SortOrder

// Create a migration object
val Migration_0_1 =
    object : DataMigration<Preferences> {
        override suspend fun shouldMigrate(currentData: Preferences): Boolean {
            return true // Always migrate to ensure new fields are added
        }

        override suspend fun cleanUp() {
            // clean up if necessary
        }

        override suspend fun migrate(currentData: Preferences): Preferences {
            // Return new preferences with default values for new fields
            return currentData.toMutablePreferences().apply {
                if (!contains(PreferencesKeys.REMINDER_DISPLAY_STYLE)) {
                    this[PreferencesKeys.REMINDER_DISPLAY_STYLE] = ReminderDisplayStyle.LIST.name
                }
                if (!contains(PreferencesKeys.SORT_ORDER)) {
                    this[PreferencesKeys.SORT_ORDER] = SortOrder.DATE.name
                }
                if (!contains(PreferencesKeys.REMINDER_STATUS)) {
                    this[PreferencesKeys.REMINDER_STATUS].toBoolean()
                }
                if (!contains(PreferencesKeys.REMINDER_FREQUENCY)) {
                    this[PreferencesKeys.REMINDER_FREQUENCY] = ReminderFrequency.WEEKLY.name
                }
                if (!contains(PreferencesKeys.NOTE_LIST_TYPE)) {
                    this[PreferencesKeys.NOTE_LIST_TYPE] = NoteListType.GRID.name
                }
            }
        }
    }
