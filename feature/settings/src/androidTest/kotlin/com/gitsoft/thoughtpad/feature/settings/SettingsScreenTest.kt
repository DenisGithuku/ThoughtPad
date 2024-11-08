
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
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsToggleable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.gitsoft.thoughtpad.core.toga.theme.ThoughtPadTheme
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
                    onNavigateBack = {},
                    onToggleSortOrder = { state.value = state.value.copy(sortOrder = it) },
                    onToggleSortDialog = { state.value = state.value.copy(isSortDialogShown = it) },
                    onTogglePeriodicReminders = {
                        state.value = state.value.copy(isPeriodicRemindersEnabled = it)
                    },
                    onToggleReminderFrequency = { state.value = state.value.copy(reminderFrequency = it) },
                    onToggleReminderStyleDialog = {
                        state.value = state.value.copy(isReminderStyleDialogShown = it)
                    },
                    onToggleReminderDisplayStyle = {
                        state.value = state.value.copy(reminderDisplayStyle = it)
                    },
                    onToggleReminderFrequencyDialog = {
                        state.value = state.value.copy(isReminderFrequencyDialogShown = it)
                    }
                )
            }
        }
    }

    @Test
    fun settingsItemIsDisplayed() {
        composeTestRule.onAllNodesWithTag(TestTags.SETTING_LIST_ITEM).onFirst().assertIsDisplayed()
    }

    @Test
    fun testSettingsItemIsClickable() {
        composeTestRule.onAllNodesWithTag(TestTags.SETTING_LIST_ITEM).onFirst().performClick()
    }

    @Test
    fun testClickThemeSettingsItemOpensDialog() {
        composeTestRule.onAllNodesWithTag(TestTags.SETTING_LIST_ITEM).onFirst().performClick()

        composeTestRule.onNodeWithTag(TestTags.THEME_COLUMN).assertIsDisplayed()
    }

    @Test
    fun testToggleableSettingsItemIsClickable() {
        composeTestRule.onAllNodesWithTag(TestTags.TOGGLEABLE_SETTING_ITEM).onFirst().performClick()

        composeTestRule
            .onAllNodesWithTag(TestTags.TOGGLEABLE_SETTING_ITEM)
            .onFirst()
            .onChildren()
            .onLast()
            .assertIsDisplayed()
        composeTestRule
            .onAllNodesWithTag(TestTags.TOGGLEABLE_SETTING_ITEM)
            .onFirst()
            .onChildren()
            .onLast()
            .assertIsToggleable()
            .performClick()
            .assertIsEnabled()

        composeTestRule
            .onAllNodesWithTag(TestTags.TOGGLEABLE_SETTING_ITEM)
            .onFirst()
            .onChildren()
            .onLast()
            .assertIsDisplayed()
            .performClick()

        composeTestRule.onAllNodesWithTag(TestTags.SETTING_LIST_ITEM)[4].assertIsDisplayed()
    }

    @Test
    fun testToggleSortDialog() {
        composeTestRule.onAllNodesWithTag(TestTags.SETTING_LIST_ITEM)[2].performClick()

        composeTestRule
            .onNodeWithTag(TestTags.SORT_ORDER_COLUMN)
            .assertIsDisplayed()
            .onChildren()
            .onFirst()
            .assertIsDisplayed()
            .assertHasClickAction()
    }

    @Test
    fun testToggleCalendarDisplayDialog() {
        composeTestRule
            .onAllNodesWithTag(TestTags.SETTING_LIST_ITEM)[1]
            .assertIsDisplayed()
            .performClick()

        composeTestRule
            .onNodeWithTag(TestTags.REMINDER_STYLE_COLUMN)
            .assertIsDisplayed()
            .onChildren()
            .onFirst()
            .assertIsDisplayed()
            .assertHasClickAction()
    }
}
