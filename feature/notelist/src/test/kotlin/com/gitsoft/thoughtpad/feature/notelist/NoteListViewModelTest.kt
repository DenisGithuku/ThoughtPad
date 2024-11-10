
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

import androidx.test.filters.MediumTest
import com.gitsoft.thoughtpad.core.common.MainCoroutineRule
import com.gitsoft.thoughtpad.core.model.NoteListType
import core.gitsoft.thoughtpad.core.data.repository.NotesRepository
import core.gitsoft.thoughtpad.core.data.repository.UserPrefsRepository
import core.gitsoft.thoughtpad.core.data.repository.fakes.FakeNotesRepository
import core.gitsoft.thoughtpad.core.data.repository.fakes.FakeUserPrefsRepository
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@MediumTest
class NoteListViewModelTest {
    @get:Rule val mainCoroutineRule by lazy { MainCoroutineRule() }

    private lateinit var userPrefsRepository: UserPrefsRepository
    private lateinit var notesRepository: NotesRepository
    private lateinit var viewModel: NoteListViewModel

    @Before
    fun setUp() {
        userPrefsRepository = FakeUserPrefsRepository()
        notesRepository = FakeNotesRepository()
        viewModel = NoteListViewModel(notesRepository, userPrefsRepository)
    }

    @Test
    fun `test state is initialized with default values`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        val state = viewModel.state.value

        assertFalse(state.isLoading)
        assertTrue(state.isDarkTheme)
    }

    @Test
    fun `test toggle note pin`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onToggleNotePin(1L, true)
        advanceUntilIdle()
        val state = viewModel.state.value
        assertTrue(state.notes.find { it.note.noteId == 1L }!!.note.isPinned)
    }

    @Test
    fun `test toggle filter dialog`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onOpenFilterDialog(true)
        val state = viewModel.state.value
        assertTrue(state.isFilterDialogVisible)
    }

    @Test
    fun `test toggle archive note`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onToggleArchive(ArchiveState(true, 1L))
        val state = viewModel.state.value
        assertTrue(state.notes.find { it.note.noteId == 1L }!!.note.isArchived)
    }

    @Test
    fun `test toggle select note`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onToggleSelectNote(viewModel.state.value.notes.first().note)
        val state = viewModel.state.value
        assertEquals(state.selectedNote, state.notes.first().note)
    }

    @Test
    fun `test toggle delete note`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onToggleDelete(DeleteState(true, 1L))
        advanceUntilIdle()
        val state = viewModel.state.value
        println(state.notes.find { it.note.noteId == 1L })
        assertTrue(state.notes.find { it.note.noteId == 1L }!!.note.isDeleted)
    }

    @Test
    fun `test toggle note list type`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
        viewModel.onToggleNoteListType(NoteListType.LIST)
        val state = viewModel.state.value
        assertEquals(state.selectedNoteListType, NoteListType.LIST)
    }
}
