
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

import androidx.test.filters.MediumTest
import com.gitsoft.thoughtpad.core.common.MainCoroutineRule
import com.gitsoft.thoughtpad.core.model.ReminderDisplayStyle
import com.gitsoft.thoughtpad.core.model.ReminderFrequency
import com.gitsoft.thoughtpad.core.model.SortOrder
import com.gitsoft.thoughtpad.core.model.ThemeConfig
import com.google.common.truth.Truth.assertThat
import core.gitsoft.thoughtpad.core.data.repository.UserPrefsRepository
import core.gitsoft.thoughtpad.core.data.repository.fakes.FakeUserPrefsRepository
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@MediumTest
class SettingsViewModelTest {
    @get:Rule val mainCoroutineRule by lazy { MainCoroutineRule() }

    private lateinit var userPrefsRepository: UserPrefsRepository
    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setUp() {
        userPrefsRepository = FakeUserPrefsRepository()
        viewModel = SettingsViewModel(userPrefsRepository)
    }

    @Test
    fun `test toggle theme`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onToggleTheme(ThemeConfig.LIGHT)
        assertThat(viewModel.state.value.selectedTheme).isEqualTo(ThemeConfig.LIGHT)
    }

    @Test
    fun `test toggle theme dialog`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onToggleThemeDialog(true)
        assertTrue(viewModel.state.value.isThemeDialogShown)
    }

    @Test
    fun `test toggle periodic reminders`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onTogglePeriodicReminders(true)
        assertTrue(viewModel.state.value.isPeriodicRemindersEnabled)
    }

    @Test
    fun `test toggle display style dialog`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onToggleReminderStyleDialog(true)
        assertTrue(viewModel.state.value.isReminderStyleDialogShown)
    }

    @Test
    fun `test toggle reminder style`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onToggleReminderDisplayStyle(ReminderDisplayStyle.CALENDAR)
        assertThat(viewModel.state.value.reminderDisplayStyle).isEqualTo(ReminderDisplayStyle.CALENDAR)
    }

    @Test
    fun `test sort order dialog`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onToggleSortDialog(true)
        assertTrue(viewModel.state.value.isSortDialogShown)
    }

    @Test
    fun `test toggle sort order`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onToggleSortOrder(SortOrder.TITLE)
        assertEquals(viewModel.state.value.sortOrder, SortOrder.TITLE)
    }

    @Test
    fun `test toggle reminder frequency dialog`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onToggleReminderFrequencyDialog(true)
        assertTrue(viewModel.state.value.isReminderFrequencyDialogShown)
    }

    @Test
    fun `test toggle reminder frequency`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onToggleReminderFrequency(ReminderFrequency.DAILY)
        assertEquals(viewModel.state.value.reminderFrequency, ReminderFrequency.DAILY)
    }
}
