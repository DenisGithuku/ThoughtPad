
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
}
