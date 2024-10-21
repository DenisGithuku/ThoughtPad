
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
import core.gitsoft.thoughtpad.core.data.repository.NotesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn

data class NoteListUiState(
    val isLoading: Boolean = false,
    val notes: List<DataWithNotesCheckListItemsAndTags> = emptyList()
)

class NoteListViewModel(notesRepository: NotesRepository) : ViewModel() {
    val state: StateFlow<NoteListUiState> =
        notesRepository.allNotes
            .mapLatest { NoteListUiState(isLoading = false, notes = it) }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                NoteListUiState(isLoading = true)
            )
}
