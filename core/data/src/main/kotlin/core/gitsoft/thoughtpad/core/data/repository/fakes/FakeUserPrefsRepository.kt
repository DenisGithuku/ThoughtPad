
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
package core.gitsoft.thoughtpad.core.data.repository.fakes

import com.gitsoft.thoughtpad.core.model.NoteListType
import com.gitsoft.thoughtpad.core.model.ReminderDisplayStyle
import com.gitsoft.thoughtpad.core.model.ReminderFrequency
import com.gitsoft.thoughtpad.core.model.SortOrder
import com.gitsoft.thoughtpad.core.model.ThemeConfig
import com.gitsoft.thoughtpad.core.model.UserPreferences
import core.gitsoft.thoughtpad.core.data.repository.UserPrefsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeUserPrefsRepository : UserPrefsRepository {

    private var _userPrefs =
        MutableStateFlow(
            UserPreferences(
                reminderDisplayStyle = ReminderDisplayStyle.LIST,
                themeConfig = ThemeConfig.DARK,
                isNotificationPermissionsGranted = true,
                isPeriodicRemindersEnabled = true,
                reminderFrequency = ReminderFrequency.DAILY,
                sortOrder = SortOrder.DATE
            )
        )

    override val userPrefs: Flow<UserPreferences>
        get() = _userPrefs

    override suspend fun updateTheme(themeConfig: ThemeConfig) {
        _userPrefs.update { it.copy(themeConfig = themeConfig) }
    }

    override suspend fun updateNotificationPermission(isGranted: Boolean) {
        _userPrefs.update { it.copy(isNotificationPermissionsGranted = isGranted) }
    }

    override suspend fun updateReminderDisplayStyle(reminderDisplayStyle: ReminderDisplayStyle) {
        _userPrefs.update { it.copy(reminderDisplayStyle = reminderDisplayStyle) }
    }

    override suspend fun updatePeriodicReminderStatus(isEnabled: Boolean) {
        _userPrefs.update { it.copy(isPeriodicRemindersEnabled = isEnabled) }
    }

    override suspend fun updatePeriodicReminderFrequency(reminderFrequency: ReminderFrequency) {
        _userPrefs.update { it.copy(reminderFrequency = reminderFrequency) }
    }

    override suspend fun updateSortOrder(sortOrder: SortOrder) {
        _userPrefs.update { it.copy(sortOrder = sortOrder) }
    }

    override suspend fun updateNoteListType(noteListType: NoteListType) {
        _userPrefs.update { it.copy(noteListType = noteListType) }
    }
}
