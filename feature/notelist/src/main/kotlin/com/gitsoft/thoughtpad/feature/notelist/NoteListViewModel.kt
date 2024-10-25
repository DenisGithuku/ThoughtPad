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
import com.gitsoft.thoughtpad.core.model.DataWithNotesCheckListItemsAndTags
import com.gitsoft.thoughtpad.core.model.Note
import core.gitsoft.thoughtpad.core.data.repository.NotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NoteListUiState(
    val isLoading: Boolean = false,
    val isFilterDialogVisible: Boolean = false,
    val selectedNote: Note? = null,
    val notes: List<DataWithNotesCheckListItemsAndTags> = emptyList()
)

class NoteListViewModel(private val notesRepository: NotesRepository) : ViewModel() {

    fun onToggleNotePin(noteId: Long, isPinned: Boolean) {
        viewModelScope.launch {

            // Fetch the note from the database
            val note = notesRepository.getNoteById(noteId)
            val updatedNote = note.copy(isPinned = isPinned)

            // Update the note in the database
            notesRepository.updateNote(updatedNote)

            // Update the selected note in the state
            _state.update {
                it.copy(selectedNote = it.selectedNote?.copy(isPinned = isPinned))
            }
        }
    }

    fun onToggleNoteFavourite(noteId: Long, isFavourite: Boolean) {
        viewModelScope.launch {
            val note = notesRepository.getNoteById(noteId)
            val updatedNote = note.copy(isFavorite = isFavourite)
            notesRepository.updateNote(updatedNote)
        }
    }

    fun onOpenFilterDialog(isOpened: Boolean) {
        _state.update {
            it.copy(isFilterDialogVisible = isOpened)
        }
    }

    fun onToggleSelectNote(note: Note?) {
        _state.update {
            it.copy(selectedNote = note)
        }
    }

    private val _state: MutableStateFlow<NoteListUiState> = MutableStateFlow(NoteListUiState())

    val state: StateFlow<NoteListUiState> = combine(
        _state, notesRepository.allNotes
    ) { state, notes ->
        state.copy(notes = notes, isLoading = false)
    }.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), NoteListUiState(isLoading = true)
        )

    override fun onCleared() {
        super.onCleared()
        _state.update {
            NoteListUiState()
        }
    }
}
