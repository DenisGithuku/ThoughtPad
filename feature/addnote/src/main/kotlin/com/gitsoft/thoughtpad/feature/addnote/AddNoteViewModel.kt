
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

import core.gitsoft.thoughtpad.core.data.repository.NotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddNoteViewModel(private val notesRepository: NotesRepository) {

    private val _state: MutableStateFlow<AddNoteUiState> = MutableStateFlow(AddNoteUiState())
    val state: StateFlow<AddNoteUiState> = _state.asStateFlow()

    fun onEvent(event: AddNoteEvent) {
        when (event) {
            is AddNoteEvent.AddNoteCheckList -> TODO()
            is AddNoteEvent.AddNoteTag -> TODO()
            is AddNoteEvent.ChangeNoteColor -> TODO()
            is AddNoteEvent.ChangeNoteText -> TODO()
            is AddNoteEvent.ChangeNoteTitle -> TODO()
            is AddNoteEvent.DeductNoteCheckList -> TODO()
            is AddNoteEvent.DeductNoteTag -> TODO()
            AddNoteEvent.Save -> TODO()
            AddNoteEvent.ToggleNoteCheckList -> TODO()
            AddNoteEvent.ToggleNoteTags -> TODO()
        }
    }

    private fun addNoteCheckList() {}

    private fun deductNoteCheckList() {}

    private fun addNoteTag() {}

    private fun deductNoteTag() {}

    private fun changeNoteColor(value: String) {}

    private fun save() {}
}
