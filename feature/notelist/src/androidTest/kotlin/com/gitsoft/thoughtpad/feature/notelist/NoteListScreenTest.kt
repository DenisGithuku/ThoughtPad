
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
package com.gitsoft.thoughtpad.feature.notelist

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import com.gitsoft.thoughtpad.core.model.DataWithNotesCheckListItemsAndTags
import com.gitsoft.thoughtpad.core.model.Note
import core.gitsoft.thoughtpad.core.toga.theme.ThoughtPadTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NoteListScreenTest {

    @get:Rule val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            ThoughtPadTheme {
                var state = remember {
                    mutableStateOf(
                        NoteListUiState(
                            notes =
                                (0..4).map {
                                    DataWithNotesCheckListItemsAndTags(
                                        note =
                                            Note(noteId = it.toLong(), noteTitle = "Title @$it", noteText = "Text @$it"),
                                        checkListItems = listOf(),
                                        tags = listOf()
                                    )
                                }
                        )
                    )
                }
                NoteListScreen(
                    state = state.value,
                    onToggleSelectedNote = { state.value = state.value.copy(selectedNote = it) },
                    onCreateNewNote = {},
                    onToggleNotePin = { id, isPinned ->
                        state.value =
                            state.value.copy(
                                notes =
                                    state.value.notes.map {
                                        if (it.note.noteId == id) {
                                            it.copy(note = it.note.copy(isPinned = isPinned))
                                        } else {
                                            it
                                        }
                                    }
                            )
                    },
                    onToggleArchiveNote = {},
                    onToggleFilterDialog = { state.value = state.value.copy(isFilterDialogVisible = it) },
                    onOpenSettings = {},
                    onOpenTags = {},
                    onToggleDeleteNote = {}
                )
            }
        }
    }

    @Test
    fun testToggleSearchBarIsDisplayedAndTakesInput() {
        composeTestRule.onNodeWithTag(TestTags.SEARCH_BAR).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.SEARCH_BAR).performTextInput("Search keyword")
    }

    @Test
    fun testNoteListExists() {
        composeTestRule.onNodeWithTag(TestTags.NOTE_LIST).assertExists()
    }

    @Test
    fun testNoteItemCardIsClickable() {
        composeTestRule.onAllNodesWithTag(TestTags.NOTE_ITEM_CARD).onLast().assertHasClickAction()
    }

    @Test
    fun testToggleNoteShowsActionsBottomSheet() {
        composeTestRule.onAllNodesWithTag(TestTags.NOTE_ITEM_CARD).onFirst().performTouchInput {
            longClick()
        }
        composeTestRule.onNodeWithTag(TestTags.NOTE_ACTIONS_BOTTOM_SHEET).assertIsDisplayed()
    }

    @Test
    fun testNoteActionItemIsClickable() {
        composeTestRule.onNodeWithTag(TestTags.NOTE_LIST).onChildAt(3).performTouchInput { longClick() }
        composeTestRule
            .onNodeWithTag(TestTags.NOTE_ACTIONS_BOTTOM_SHEET)
            .onChildAt(3)
            .assertHasClickAction()
    }

    @Test
    fun testAddNoteFabIsClickable() {
        composeTestRule.onNodeWithTag(TestTags.ADD_NOTE_FAB).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.ADD_NOTE_FAB).assertHasClickAction()
    }

    @Test
    fun testToggleSideDrawerIsDisplayed() {
        composeTestRule.onNodeWithTag(TestTags.SIDEBAR_TOGGLE).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.SIDEBAR_TOGGLE).assertHasClickAction()
        composeTestRule.onNodeWithTag(TestTags.SIDEBAR_TOGGLE).performClick()
        composeTestRule.onNodeWithTag(TestTags.SIDEBAR).assertIsDisplayed()
    }
}
