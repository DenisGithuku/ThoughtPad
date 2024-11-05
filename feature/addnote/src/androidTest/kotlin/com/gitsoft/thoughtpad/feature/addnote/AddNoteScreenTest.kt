
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
package com.gitsoft.thoughtpad.feature.addnote

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.gitsoft.thoughtpad.core.model.Note
import com.gitsoft.thoughtpad.core.model.NoteColor
import com.gitsoft.thoughtpad.core.toga.theme.ThoughtPadTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddNoteScreenTest {

    @get:Rule val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            ThoughtPadTheme {
                val state = remember {
                    mutableStateOf(
                        AddNoteUiState(
                            note =
                                Note(
                                    noteTitle = "Test title",
                                    noteText = "Test text",
                                    color = NoteColor.Blue,
                                    isPinned = false,
                                    isDeleted = false,
                                    isArchived = false,
                                    noteId = 0L,
                                    createdAt = System.currentTimeMillis(),
                                    updatedAt = System.currentTimeMillis(),
                                    isCheckList = false,
                                    reminderTime = null
                                )
                        )
                    )
                }
                AddNoteScreen(
                    state = state.value,
                    onNavigateBack = {},
                    onDiscardNote = {
                        state.value = state.value.copy(note = state.value.note.copy(isDeleted = true))
                    },
                    onSave = {},
                    onAddNewTag = {
                        state.value = state.value.copy(selectedTags = state.value.defaultTags.plus(it))
                    },
                    onChangeNoteColor = {
                        state.value = state.value.copy(note = state.value.note.copy(color = it))
                    },
                    onRemoveTag = {
                        state.value =
                            state.value.copy(
                                selectedTags = state.value.selectedTags.filterNot { tag -> tag.tagId == it.tagId }
                            )
                    },
                    onTogglePin = {
                        state.value =
                            state.value.copy(note = state.value.note.copy(isPinned = !state.value.note.isPinned))
                    },
                    onChangeDate = {
                        state.value = state.value.copy(note = state.value.note.copy(reminderTime = it))
                    },
                    onChangeTime = {
                        state.value = state.value.copy(note = state.value.note.copy(reminderTime = it))
                    },
                    onToggleTags = { state.value = state.value.copy(hasTags = it) },
                    onChangeTitle = {
                        state.value = state.value.copy(note = state.value.note.copy(noteTitle = it))
                    },
                    onChangeContent = {
                        state.value = state.value.copy(note = state.value.note.copy(noteText = it))
                    },
                    onToggleReminder = {
                        state.value =
                            state.value.copy(
                                note =
                                    state.value.note.copy(
                                        reminderTime =
                                            if (state.value.note.reminderTime == null) {
                                                System.currentTimeMillis()
                                            } else {
                                                null
                                            }
                                    )
                            )
                    },
                    onAddCheckListItem = {
                        state.value = state.value.copy(checkListItems = state.value.checkListItems.plus(it))
                    },
                    onChangeTagColor = { state.value = state.value.copy(selectedTagColor = it) },
                    onToggleColorBar = { state.value = state.value.copy(isColorVisible = it) },
                    onToggleTagSheet = { state.value = state.value.copy(isTagSheetVisible = it) },
                    onToggleCheckList = {
                        state.value =
                            state.value.copy(
                                note = state.value.note.copy(isCheckList = !state.value.note.isCheckList)
                            )
                    },
                    onToggleDateSheet = { state.value = state.value.copy(isDateSheetVisible = it) },
                    onToggleDateDialog = { state.value = state.value.copy(dateDialogIsVisible = it) },
                    onToggleTimeDialog = { state.value = state.value.copy(timeDialogIsVisible = it) },
                    onToggleTagSelection = {
                        state.value =
                            state.value.copy(
                                selectedTags =
                                    if (state.value.selectedTags.contains(it)) {
                                        state.value.selectedTags.minus(it)
                                    } else {
                                        state.value.selectedTags.plus(it)
                                    }
                            )
                    },
                    onRemoveCheckListItem = { item ->
                        state.value =
                            state.value.copy(
                                checkListItems =
                                    state.value.checkListItems.filter { it.checkListItemId != item.checkListItemId }
                            )
                    },
                    onCheckListItemCheckedChange = { item, isChecked ->
                        state.value =
                            state.value.copy(
                                checkListItems =
                                    state.value.checkListItems.map {
                                        if (it.checkListItemId == item.checkListItemId) {
                                            it.copy(isChecked = isChecked)
                                        } else {
                                            it
                                        }
                                    }
                            )
                    },
                    updatePermissionsStatus = {
                        state.value = state.value.copy(permissionsNotificationsGranted = true)
                    }
                )
            }
        }
    }

    @Test
    fun testNoteTitleInputExistsAndIsEditable() {
        composeTestRule.onNodeWithTag(TestTags.NOTE_TITLE_INPUT).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.NOTE_TITLE_INPUT).performTextClearance()
        composeTestRule.onNodeWithTag(TestTags.NOTE_TITLE_INPUT).performTextInput("New title")
        composeTestRule.onNodeWithTag(TestTags.NOTE_TITLE_INPUT).assertTextContains("New title")
    }

    @Test
    fun testNoteContentInputExistsAndIsEditable() {
        composeTestRule.onNodeWithTag(TestTags.NOTE_TEXT_INPUT).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.NOTE_TEXT_INPUT).performTextClearance()
        composeTestRule.onNodeWithTag(TestTags.NOTE_TEXT_INPUT).performTextInput("New text")
        composeTestRule.onNodeWithTag(TestTags.NOTE_TEXT_INPUT).assertTextContains("New text")
    }

    @Test
    fun testTogglePinButtonExists() {
        composeTestRule.onNodeWithTag(TestTags.TOGGLE_PIN_BUTTON).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.TOGGLE_PIN_BUTTON).performClick()
    }

    @Test
    fun testCheckListButtonExistsAndIsToggleable() {
        composeTestRule.onNodeWithTag(TestTags.TOGGLE_CHECKLIST_BUTTON).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.TOGGLE_CHECKLIST_BUTTON).performClick()
        composeTestRule.onNodeWithTag(TestTags.TOGGLE_CHECKLIST_BUTTON).assertIsOn()
    }

    @Test
    fun testSaveButtonIsDisabledWhenNoteTitleOrContentIsEmpty() {
        composeTestRule.onNodeWithTag(TestTags.SAVE_BUTTON).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.NOTE_TITLE_INPUT).performTextClearance()
        composeTestRule.onNodeWithTag(TestTags.NOTE_TEXT_INPUT).performTextClearance()
        composeTestRule.onNodeWithTag(TestTags.SAVE_BUTTON).assertIsNotEnabled()
    }

    @Test
    fun testSaveButtonIsEnabledWhenNoteTitleOrContentIsNotEmpty() {
        composeTestRule.onNodeWithTag(TestTags.SAVE_BUTTON).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.NOTE_TITLE_INPUT).performTextInput("New title")
        composeTestRule.onNodeWithTag(TestTags.NOTE_TEXT_INPUT).performTextInput("New text")
        composeTestRule.onNodeWithTag(TestTags.SAVE_BUTTON).assertIsEnabled()
        composeTestRule.onNodeWithTag(TestTags.SAVE_BUTTON).performClick()
    }

    @Test
    fun testCheckListIsShownWhenCheckListButtonIsClicked() {
        composeTestRule.onNodeWithTag(TestTags.TOGGLE_CHECKLIST_BUTTON).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.TOGGLE_CHECKLIST_BUTTON).performClick()
        composeTestRule.onNodeWithTag(TestTags.CHECK_LIST).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.TOGGLE_CHECKLIST_BUTTON).performClick()
        composeTestRule.onNodeWithTag(TestTags.CHECK_LIST).assertIsNotDisplayed()
    }

    @Test
    fun testTagListIsShownWhenTagButtonIsToggled() {
        composeTestRule.onNodeWithTag(TestTags.NOTE_TAG_TOGGLE).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.NOTE_TAG_TOGGLE).performClick()
        composeTestRule.onNodeWithTag(TestTags.TAG_LIST).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.NOTE_TAG_TOGGLE).performClick()
        composeTestRule.onNodeWithTag(TestTags.TAG_LIST).assertIsNotDisplayed()
    }

    @Test
    fun testColorBarIsShownWhenColorBarButtonIsToggled() {
        composeTestRule.onNodeWithTag(TestTags.TOGGLE_COLOR_BUTTON).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.TOGGLE_COLOR_BUTTON).performClick()
        composeTestRule.onNodeWithTag(TestTags.NOTE_COLOR_PICKER).assertIsDisplayed()
    }

    @Test
    fun testAddTagDialogIsShownWhenAddTagButtonIsClicked() {
        composeTestRule.onNodeWithTag(TestTags.NOTE_TAG_TOGGLE).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.NOTE_TAG_TOGGLE).performClick()
        composeTestRule.onNodeWithTag(TestTags.ADD_TAG_BUTTON).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.ADD_TAG_BUTTON).performClick()
        composeTestRule.onNodeWithTag(TestTags.TAG_BOTTOM_SHEET).assertIsDisplayed()
    }

    @Test
    fun testAddCheckListButtonTogglesEnabledStateOnTextChange() {
        composeTestRule.onNodeWithTag(TestTags.TOGGLE_CHECKLIST_BUTTON).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.TOGGLE_CHECKLIST_BUTTON).performClick()
        composeTestRule.onNodeWithTag(TestTags.CHECK_LIST).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.ADD_NEW_CHECKLIST_ITEM_BUTTON).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.ADD_NEW_CHECKLIST_ITEM_BUTTON).assertIsNotEnabled()
        composeTestRule
            .onNodeWithTag(TestTags.NEW_CHECKLIST_ITEM_TEXT_FIELD)
            .performTextInput("New item")
        composeTestRule.onNodeWithTag(TestTags.ADD_NEW_CHECKLIST_ITEM_BUTTON).assertIsEnabled()
    }
}
