
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
package com.gitsoft.thoughtpad.feature.tags

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToKey
import androidx.compose.ui.test.performTextInput
import com.gitsoft.thoughtpad.core.model.Tag
import com.gitsoft.thoughtpad.core.model.TagColor
import core.gitsoft.thoughtpad.core.toga.theme.ThoughtPadTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TagScreenTest {

    @get:Rule val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            ThoughtPadTheme {
                var state = remember {
                    mutableStateOf(
                        TagUiState(
                            tags =
                                (0..15).map {
                                    Tag(tagId = it.toLong(), name = "Tag name @$it", color = TagColor.Brown)
                                }
                        )
                    )
                }

                TagScreen(
                    state = state.value,
                    onNavigateBack = {},
                    onToggleAddTag = { state.value = state.value.copy(isAddTag = it) },
                    onToggleEditTag = { state.value = state.value.copy(selectedTag = it) },
                    onChangeTagColor = { state.value = state.value.copy(selectedTagColor = it) },
                    onChangeTagName = { state.value = state.value.copy(tagName = it) },
                    onEditTagName = {
                        state.value = state.value.copy(selectedTag = state.value.selectedTag?.copy(name = it))
                    },
                    onEditTagColor = {
                        state.value = state.value.copy(selectedTag = state.value.selectedTag?.copy(color = it))
                    },
                    onTagUpdated = { state.value = state.value.copy(isLoading = false) },
                    onSaveNewTag = {
                        val tags = state.value.tags.toMutableList()
                        tags.add(it)
                        state.value = state.value.copy(tags = tags)
                    },
                    onDeleteTag = {
                        val tags = state.value.tags.toMutableList()
                        tags.remove(it)
                        state.value = state.value.copy(tags = tags, deletedTag = it)
                    },
                    onResetDeletedTag = { state.value = state.value.copy(deletedTag = null) },
                    onUndoDeleteTag = {}
                )
            }
        }
    }

    @Test
    fun testTagListIsDisplayed() {
        composeTestRule.onNodeWithTag(TestTag.TAG_LIST).assertExists()
        composeTestRule.onNodeWithTag(TestTag.TAG_LIST).performScrollToKey(8L)
    }

    @Test
    fun testNewTagButtonHasClickAction() {
        composeTestRule.onNodeWithTag(TestTag.TAG_LIST).performScrollToKey(15L)
        composeTestRule.onNodeWithTag(TestTag.ADD_TAG_BTN).assertHasClickAction()
    }

    @Test
    fun testNewTagButtonOpensBottomSheet() {
        composeTestRule.onNodeWithTag(TestTag.TAG_LIST).performScrollToKey(15L)
        composeTestRule.onNodeWithTag(TestTag.ADD_TAG_BTN).performClick()
        composeTestRule.onNodeWithTag(TestTag.TAG_SAVE_UPDATE_BOTTOM_SHEET).assertIsDisplayed()
    }

    @Test
    fun testNewTagInput() {
        composeTestRule.onNodeWithTag(TestTag.TAG_LIST).performScrollToKey(15L)
        composeTestRule.onNodeWithTag(TestTag.ADD_TAG_BTN).performClick()
        composeTestRule.onNodeWithTag(TestTag.NEW_TAG_INPUT).performTextInput("New tag")
    }

    @Test
    fun testEditTagInput() {
        composeTestRule.onAllNodesWithTag(TestTag.EDIT_TAG_BTN).onFirst().performClick()
        composeTestRule
            .onNodeWithTag(TestTag.EDIT_TAG_INPUT)
            .assertExists()
            .performTextInput("Edit tag")
    }

    @Test
    fun testDeleteBtnExists() {
        composeTestRule
            .onAllNodesWithTag(TestTag.DELETE_TAG_BTN)
            .onFirst()
            .assertExists()
            .performClick()
    }

    @Test
    fun testSaveButtonIsDisabledOnEmptyInput() {
        composeTestRule.onNodeWithTag(TestTag.TAG_LIST).performScrollToKey(15L)
        composeTestRule.onNodeWithTag(TestTag.ADD_TAG_BTN).performClick()
        composeTestRule.onNodeWithTag(TestTag.SAVE_TAG_BTN).assertIsNotEnabled()
    }

    @Test
    fun testUpdateButtonIsDisplayed() {
        composeTestRule.onAllNodesWithTag(TestTag.EDIT_TAG_BTN).onFirst().performClick()
        composeTestRule.onNodeWithTag(TestTag.UPDATE_TAG_BTN).assertIsDisplayed()
    }
}
