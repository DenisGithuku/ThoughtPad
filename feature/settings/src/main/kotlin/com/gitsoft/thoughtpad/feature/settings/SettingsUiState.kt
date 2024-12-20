
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
package com.gitsoft.thoughtpad.feature.settings

import com.gitsoft.thoughtpad.core.model.ReminderDisplayStyle
import com.gitsoft.thoughtpad.core.model.ReminderFrequency
import com.gitsoft.thoughtpad.core.model.SortOrder
import com.gitsoft.thoughtpad.core.model.ThemeConfig

data class SettingsUiState(
    val isPeriodicRemindersEnabled: Boolean = false,
    val reminderDisplayStyle: ReminderDisplayStyle = ReminderDisplayStyle.LIST,
    val availableReminderDisplayStyles: List<ReminderDisplayStyle> =
        listOf(ReminderDisplayStyle.LIST, ReminderDisplayStyle.CALENDAR),
    val sortOrder: SortOrder = SortOrder.DATE,
    val isSortDialogShown: Boolean = false,
    val availableSortOrders: List<SortOrder> = listOf(SortOrder.DATE, SortOrder.TITLE),
    val isReminderFrequencyDialogShown: Boolean = false,
    val availableReminderFrequencies: List<ReminderFrequency> =
        listOf(ReminderFrequency.DAILY, ReminderFrequency.WEEKLY),
    val isReminderStyleDialogShown: Boolean = false,
    val reminderFrequency: ReminderFrequency = ReminderFrequency.WEEKLY,
    val selectedTheme: ThemeConfig = ThemeConfig.LIGHT,
    val isThemeDialogShown: Boolean = false,
    val isAppInfoDialogShown: Boolean = false,
    val availableThemes: List<ThemeConfig> = listOf(ThemeConfig.LIGHT, ThemeConfig.DARK)
)
