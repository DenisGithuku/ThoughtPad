
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

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import core.gitsoft.thoughtpad.core.toga.theme.ThoughtPadTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SettingsScreenTest {

    @get:Rule val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            ThoughtPadTheme {
                val state = remember { mutableStateOf(SettingsUiState()) }
                SettingsScreen(
                    state = state.value,
                    onToggleTheme = { state.value = state.value.copy(selectedTheme = it) },
                    onToggleThemeDialog = { state.value = state.value.copy(isThemeDialogShown = it) },
                    onNavigateBack = {}
                )
            }
        }
    }

    @Test
    fun settingsItemIsDisplayed() {
        composeTestRule.onNodeWithTag(TestTags.SETTINGS_ITEM).assertIsDisplayed()
    }

    @Test
    fun testSettingsItemIsClickable() {
        composeTestRule.onNodeWithTag(TestTags.SETTINGS_ITEM).performClick()
    }

    @Test
    fun testClickThemeSettingsItemOpensDialog() {
        composeTestRule.onNodeWithTag(TestTags.SETTINGS_ITEM).performClick()

        composeTestRule.onNodeWithTag(TestTags.THEME_COLUMN).assertIsDisplayed()
    }
}