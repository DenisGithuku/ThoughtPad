
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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gitsoft.thoughtpad.core.model.Note
import com.gitsoft.thoughtpad.core.model.NoteListType
import com.gitsoft.thoughtpad.core.model.SortOrder
import com.gitsoft.thoughtpad.core.model.ThemeConfig
import core.gitsoft.thoughtpad.core.data.repository.NotesRepository
import core.gitsoft.thoughtpad.core.data.repository.UserPrefsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NoteListViewModel(
    private val notesRepository: NotesRepository,
    private val userPrefsRepository: UserPrefsRepository
) : ViewModel() {

    fun onToggleNotePin(noteId: Long, isPinned: Boolean) {
        viewModelScope.launch {
            // Fetch the note from the database
            val note = notesRepository.getNoteById(noteId)
            val updatedNote = note.copy(isPinned = isPinned)

            // Update the note in the database
            notesRepository.updateNote(updatedNote)

            // Update the selected note in the state
            _state.update { it.copy(selectedNote = it.selectedNote?.copy(isPinned = isPinned)) }
        }
    }

    fun onOpenFilterDialog(isOpened: Boolean) {
        _state.update { it.copy(isFilterDialogVisible = isOpened) }
    }

    fun onToggleSelectNote(note: Note?) {
        _state.update { it.copy(selectedNote = note) }
    }

    fun onToggleDelete(deleteState: DeleteState) {
        viewModelScope.launch {
            if (deleteState.noteId == null) {
                _state.update { it.copy(deleteState = DeleteState()) }
            } else {
                val note = notesRepository.getNoteById(deleteState.noteId)
                notesRepository.updateNote(
                    note.copy(isDeleted = deleteState.isDeleted, updatedAt = System.currentTimeMillis())
                )
                // Update deleted state in case reversal happens
                _state.update { it.copy(deleteState = deleteState) }
            }
        }
    }

    fun onToggleArchive(archiveState: ArchiveState) {
        viewModelScope.launch {
            if (archiveState.noteId == null) {
                // Restore delete state to default value
                _state.update { it.copy(archiveState = ArchiveState()) }
            } else {
                val note = notesRepository.getNoteById(archiveState.noteId)
                notesRepository.updateNote(note.copy(isArchived = archiveState.isArchived))

                // Update archived state in case reversal happens
                _state.update { it.copy(archiveState = archiveState) }
            }
        }
    }

    fun onToggleNoteListType(noteListType: NoteListType) {
        viewModelScope.launch { userPrefsRepository.updateNoteListType(noteListType) }
    }

    private val _state: MutableStateFlow<NoteListUiState> = MutableStateFlow(NoteListUiState())

    val state: StateFlow<NoteListUiState> =
        combine(_state, notesRepository.allNotes, userPrefsRepository.userPrefs) { state, notes, prefs
                ->
                state.copy(
                    notes =
                        when (prefs.sortOrder) {
                            SortOrder.TITLE -> notes.sortedBy { it.note.noteTitle }
                            SortOrder.DATE -> notes.sortedByDescending { it.note.createdAt }
                        },
                    isDarkTheme = prefs.themeConfig == ThemeConfig.DARK,
                    isLoading = false,
                    selectedNoteListType = prefs.noteListType
                )
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                NoteListUiState(isLoading = true)
            )

    override fun onCleared() {
        super.onCleared()
        _state.update { NoteListUiState() }
    }
}
