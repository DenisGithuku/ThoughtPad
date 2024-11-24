
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

import androidx.lifecycle.SavedStateHandle
import androidx.test.filters.MediumTest
import com.gitsoft.thoughtpad.core.common.MainCoroutineRule
import com.gitsoft.thoughtpad.core.model.CheckListItem
import com.gitsoft.thoughtpad.core.model.NoteColor
import com.gitsoft.thoughtpad.core.model.Tag
import com.gitsoft.thoughtpad.core.model.TagColor
import com.google.common.truth.Truth.assertThat
import core.gitsoft.thoughtpad.core.data.repository.NotesRepository
import core.gitsoft.thoughtpad.core.data.repository.UserPrefsRepository
import core.gitsoft.thoughtpad.core.data.repository.fakes.FakeNotesRepository
import core.gitsoft.thoughtpad.core.data.repository.fakes.FakeUserPrefsRepository
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertNull

@MediumTest
class AddNoteViewModelTest {

    @get:Rule val mainCoroutineRule by lazy { MainCoroutineRule() }

    private lateinit var userPrefsRepository: UserPrefsRepository
    private lateinit var notesRepository: NotesRepository
    private lateinit var viewModel: AddNoteViewModel

    @Before
    fun setUp() {
        userPrefsRepository = FakeUserPrefsRepository()
        notesRepository = FakeNotesRepository()
        viewModel = AddNoteViewModel(SavedStateHandle(), notesRepository, userPrefsRepository)
    }

    @Test
    fun `test initialize note detail state with an existing note populates data`() = runTest {
        viewModel =
            AddNoteViewModel(
                SavedStateHandle(mapOf("noteId" to 1.toLong())),
                notesRepository,
                userPrefsRepository
            )
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        val state = viewModel.state.value
        advanceUntilIdle()

        assertThat(state.note.noteId).isEqualTo(1L)

        assertThat(state.systemInDarkMode).isTrue()
    }

    @Test
    fun `test initialize empty note detail state returns null values`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        val state = viewModel.state.value
        advanceUntilIdle()

        assertThat(state.note.noteTitle).isNull()

        assertThat(state.note.color).isEqualTo(NoteColor.Default)
    }

    @Test
    fun `test add checklist item`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }

        val checkListItem = CheckListItem(noteId = 1L, text = "Go to the gym", isChecked = true)
        viewModel.onEvent(AddNoteEvent.AddCheckListItem(checkListItem))
        advanceUntilIdle()
        val state = viewModel.state.value
        assertContains(state.checkListItems.map { it.checkListItemId }, 0L)
    }

    @Test
    fun `test remove checklist item`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }

        val checkListItem = CheckListItem(noteId = 1L, text = "Go to the gym", isChecked = true)
        viewModel.onEvent(AddNoteEvent.AddCheckListItem(checkListItem))

        viewModel.onEvent(AddNoteEvent.RemoveCheckListItem(checkListItem))
        advanceUntilIdle()
        val state = viewModel.state.value
        assertTrue(state.checkListItems.map { it.text }.none { it == checkListItem.text })
    }

    @Test
    fun `test add tag existing tag is not added`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        val tag = Tag(name = "Hobby", color = TagColor.Brown)
        viewModel.onEvent(AddNoteEvent.AddTag(tag))
        advanceUntilIdle()

        assertContains(viewModel.state.value.defaultTags.map { it.name }, tag.name)

        assertContains(viewModel.state.value.selectedTags.map { it.name }, tag.name)
    }

    @Test
    fun `test add tag new tag is added`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        val tag = Tag(name = "School", color = TagColor.Yellow)
        viewModel.onEvent(AddNoteEvent.AddTag(tag))
        advanceUntilIdle()
        val state = viewModel.state.value
        assertContains(state.defaultTags.map { it.name }, tag.name)

        assertContains(state.selectedTags.map { it.name }, tag.name)
    }

    @Test
    fun `test remove tag`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        val tag = Tag(name = "School", color = TagColor.Yellow)
        viewModel.onEvent(AddNoteEvent.AddTag(tag))
        viewModel.onEvent(AddNoteEvent.RemoveTag(viewModel.state.value.selectedTags.first()))
        advanceUntilIdle()

        assertThat(viewModel.state.value.selectedTags.map { it.name }).doesNotContain(tag.name)
    }

    @Test
    fun `test change note color`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onEvent(AddNoteEvent.ChangeNoteColor(value = NoteColor.BurntOrange))
        advanceUntilIdle()

        assertEquals(viewModel.state.value.selectedNoteColor, NoteColor.BurntOrange)
    }

    @Test
    fun `test change tag color`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onEvent(AddNoteEvent.ChangeTagColor(value = TagColor.Purple))
        advanceUntilIdle()

        assertEquals(viewModel.state.value.selectedTagColor, TagColor.Purple)
    }

    @Test
    fun `test change note text`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onEvent(AddNoteEvent.ChangeText(value = "New text"))
        advanceUntilIdle()

        assertEquals(viewModel.state.value.note.noteText, "New text")
    }

    @Test
    fun `test change note title`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onEvent(AddNoteEvent.ChangeTitle(value = "New title"))
        advanceUntilIdle()

        assertEquals(viewModel.state.value.note.noteTitle, "New title")
    }

    @Test
    fun `test toggle checklist`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onEvent(AddNoteEvent.ToggleCheckList(true))
        advanceUntilIdle()

        assertTrue(viewModel.state.value.note.isCheckList)
    }

    @Test
    fun `test toggle tags`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onEvent(AddNoteEvent.ToggleTags(true))
        advanceUntilIdle()

        assertTrue(viewModel.state.value.hasTags)
    }

    @Test
    fun `test toggle tag selection`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        val tag = viewModel.state.value.defaultTags.first()
        viewModel.onEvent(AddNoteEvent.ToggleTagSelection(tag))
        advanceUntilIdle()
        assertContains(viewModel.state.value.selectedTags, tag)
    }

    @Test
    fun `test toggle pin`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onEvent(AddNoteEvent.TogglePin(true))
        advanceUntilIdle()

        assertTrue(viewModel.state.value.note.isPinned)
    }

    @Test
    fun `test toggle selected tag is removed`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onEvent(AddNoteEvent.AddTag(Tag(name = "School", color = TagColor.Yellow)))
        val tag = viewModel.state.value.selectedTags.first()
        viewModel.onEvent(AddNoteEvent.ToggleTagSelection(tag))
        advanceUntilIdle()
        assertTrue(viewModel.state.value.selectedTags.map { it.name }.none { it == tag.name })
    }

    @Test
    fun `test toggle unselected tag is selected`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        val tag = viewModel.state.value.defaultTags.first()
        viewModel.onEvent(AddNoteEvent.ToggleTagSelection(tag))
        advanceUntilIdle()
        assertContains(viewModel.state.value.selectedTags, tag)
    }

    @Test
    fun `test toggle date sheet`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onEvent(AddNoteEvent.ToggleDateSheet(true))
        advanceUntilIdle()
        assertTrue(viewModel.state.value.isDateSheetVisible)
    }

    @Test
    fun `test toggle tag sheet`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onEvent(AddNoteEvent.ToggleTagSheet(true))
        advanceUntilIdle()
        assertTrue(viewModel.state.value.isTagSheetVisible)
    }

    @Test
    fun `test toggle color bar`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onEvent(AddNoteEvent.ToggleColorBar(true))
        advanceUntilIdle()
        assertTrue(viewModel.state.value.isColorVisible)
    }

    @Test
    fun `test toggle date dialog`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onEvent(AddNoteEvent.ToggleDateDialog(true))
        advanceUntilIdle()
        assertTrue(viewModel.state.value.dateDialogIsVisible)
    }

    @Test
    fun `test toggle time dialog`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onEvent(AddNoteEvent.ToggleTimeDialog(true))
        advanceUntilIdle()
        assertTrue(viewModel.state.value.timeDialogIsVisible)
    }

    @Test
    fun `test toggle reminders`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onEvent(AddNoteEvent.ToggleReminders(true))
        advanceUntilIdle()
        assertTrue(viewModel.state.value.hasReminder)
    }

    @Test
    fun `test toggle check list item checked change`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        val checkListItem = CheckListItem(noteId = 1L, text = "Go to the gym", isChecked = false)
        viewModel.onEvent(AddNoteEvent.AddCheckListItem(checkListItem))
        viewModel.onEvent(AddNoteEvent.CheckListItemCheckedChange(checkListItem, true))
        advanceUntilIdle()
        assertTrue(viewModel.state.value.checkListItems.first().isChecked)
    }

    @Test
    fun `test discard note`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onEvent(AddNoteEvent.DiscardNote)
        advanceUntilIdle()
        assertTrue(viewModel.state.value.deletedSuccessfully)
    }

    @Test
    fun `test update notification permissions`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onEvent(AddNoteEvent.UpdateNotificationPermissions)
        advanceUntilIdle()
        assertTrue(viewModel.state.value.permissionsNotificationsGranted)
    }

    @Test
    fun `test save new note`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onEvent(AddNoteEvent.ChangeTitle(value = "New title"))
        viewModel.onEvent(AddNoteEvent.ChangeText(value = "New text"))
        viewModel.onEvent(AddNoteEvent.ChangeNoteColor(value = NoteColor.Lavender))
        viewModel.onEvent(AddNoteEvent.ToggleTags(true))
        viewModel.onEvent(AddNoteEvent.AddTag(Tag(name = "School", color = TagColor.Yellow)))
        viewModel.onEvent(AddNoteEvent.AddTag(Tag(name = "Fun", color = TagColor.Blue)))
        viewModel.onEvent(AddNoteEvent.AddTag(Tag(name = "Private", color = TagColor.Purple)))
        viewModel.onEvent(AddNoteEvent.ToggleCheckList(true))
        viewModel.onEvent(
            AddNoteEvent.AddCheckListItem(
                CheckListItem(noteId = 1L, text = "New check list item", isChecked = true)
            )
        )
        viewModel.onEvent(AddNoteEvent.ToggleReminders(true))
        viewModel.onEvent(AddNoteEvent.TogglePin(true))
        viewModel.onEvent(AddNoteEvent.Save)
        advanceUntilIdle()
        val hasSaved =
            notesRepository.allNotes.first().any {
                it.note.noteTitle == "New title" &&
                    it.note.noteText == "New text" &&
                    it.note.color == NoteColor.Lavender &&
                    it.tags.any { it.name == "School" } &&
                    it.tags.any { it.name == "Fun" } &&
                    it.tags.any { it.name == "Private" } &&
                    it.checkListItems.any { it.text == "New check list item" } &&
                    it.checkListItems.first().isChecked &&
                    it.note.reminderTime != null &&
                    it.note.isPinned
            }

        assertTrue(hasSaved)
    }

    @Test
    fun `test update note`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel =
            AddNoteViewModel(
                SavedStateHandle(mapOf("noteId" to 1.toLong())),
                notesRepository,
                userPrefsRepository
            )
        viewModel.onEvent(AddNoteEvent.ChangeTitle(value = "New title"))
        viewModel.onEvent(AddNoteEvent.ChangeText(value = "New text"))
        viewModel.onEvent(AddNoteEvent.ChangeNoteColor(value = NoteColor.Lavender))
        viewModel.onEvent(AddNoteEvent.ToggleTags(true))
        viewModel.onEvent(AddNoteEvent.AddTag(Tag(name = "School", color = TagColor.Yellow)))
        viewModel.onEvent(
            AddNoteEvent.AddCheckListItem(
                CheckListItem(noteId = 1L, text = "New check list item", isChecked = true)
            )
        )
        viewModel.onEvent(AddNoteEvent.Save)
        advanceUntilIdle()
        val hasUpdated =
            notesRepository.allNotes.first().any {
                it.note.noteTitle == "New title" &&
                    it.note.noteText == "New text" &&
                    it.note.color == NoteColor.Lavender &&
                    it.checkListItems.any { it.text == "New check list item" } &&
                    it.checkListItems.first().isChecked &&
                    it.tags.any { it.name == "School" }
            }
        assertTrue(hasUpdated)
    }

    @Test
    fun testChangePassword() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onEvent(AddNoteEvent.ChangePassword("password"))
        assertEquals(viewModel.state.value.password, "password")

        viewModel.onEvent(AddNoteEvent.ChangePassword(null))
        assertEquals(viewModel.state.value.password, null)
    }

    @Test
    fun togglePasswordSheet() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onEvent(AddNoteEvent.TogglePasswordDialog(true))
        assertTrue(viewModel.state.value.isPasswordSheetVisible)
    }

    @Test
    fun removePassWord() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onEvent(AddNoteEvent.RemovePassword)
        assertNull(viewModel.state.value.password)
        assertNull(viewModel.state.value.encryptedPassword)
    }
}
