
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
package core.gitsoft.thoughtpad.core.data.repository

import com.gitsoft.thoughtpad.core.datastore.UserPrefsDataSource
import com.gitsoft.thoughtpad.core.model.NoteListType
import com.gitsoft.thoughtpad.core.model.ReminderDisplayStyle
import com.gitsoft.thoughtpad.core.model.ReminderFrequency
import com.gitsoft.thoughtpad.core.model.SortOrder
import com.gitsoft.thoughtpad.core.model.ThemeConfig
import com.gitsoft.thoughtpad.core.model.UserPreferences
import kotlinx.coroutines.flow.Flow

class UserPrefsRepositoryImpl(private val userPrefsDataSource: UserPrefsDataSource) :
    UserPrefsRepository {

    override val userPrefs: Flow<UserPreferences>
        get() = userPrefsDataSource.userPrefs

    override suspend fun updateTheme(themeConfig: ThemeConfig) {
        userPrefsDataSource.updateTheme(themeConfig)
    }

    override suspend fun updateNotificationPermission(isGranted: Boolean) {
        userPrefsDataSource.updateNotificationPermission(isGranted)
    }

    override suspend fun updateReminderDisplayStyle(reminderDisplayStyle: ReminderDisplayStyle) {
        userPrefsDataSource.updateReminderDisplayStyle(reminderDisplayStyle)
    }

    override suspend fun updatePeriodicReminderStatus(isEnabled: Boolean) {
        userPrefsDataSource.updatePeriodicReminderStatus(isEnabled)
    }

    override suspend fun updatePeriodicReminderFrequency(reminderFrequency: ReminderFrequency) {
        userPrefsDataSource.updatePeriodicReminderFrequency(reminderFrequency)
    }

    override suspend fun updateSortOrder(sortOrder: SortOrder) {
        userPrefsDataSource.updateSortOrder(sortOrder)
    }

    override suspend fun updateNoteListType(noteListType: NoteListType) {
        userPrefsDataSource.updateNoteListType(noteListType)
    }
}
