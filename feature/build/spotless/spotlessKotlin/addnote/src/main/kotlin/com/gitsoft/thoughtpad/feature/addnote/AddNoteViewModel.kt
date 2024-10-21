
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

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.gitsoft.thoughtpad.core.model.CheckListItem
import com.gitsoft.thoughtpad.core.model.Tag
import core.gitsoft.thoughtpad.core.data.repository.NotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AddNoteViewModel(private val notesRepository: NotesRepository) : ViewModel() {

    private val _state: MutableStateFlow<AddNoteUiState> = MutableStateFlow(AddNoteUiState())
    val state: StateFlow<AddNoteUiState> = _state.asStateFlow()

    fun onEvent(event: AddNoteEvent) {
        when (event) {
            is AddNoteEvent.AddNoteCheckListItem -> addNoteCheckList(event.checkListItem)
            is AddNoteEvent.AddNoteTag -> addNoteTag(event.tag)
            is AddNoteEvent.ChangeNoteColor -> changeNoteColor(event.value)
            is AddNoteEvent.ChangeNoteText -> changeNoteText(event.value)
            is AddNoteEvent.ChangeNoteTitle -> changeNoteTitle(event.value)
            is AddNoteEvent.RemoveCheckListItem -> removeCheckListItem(event.checkListItem)
            is AddNoteEvent.RemoveTag -> removeNoteTag(event.tag)
            is AddNoteEvent.ToggleNoteCheckList -> toggleNoteCheckList(event.value)
            is AddNoteEvent.ToggleNoteTags -> toggleNoteTags(event.value)
            is AddNoteEvent.ToggleColorBar -> toggleColorBar(event.value)
            is AddNoteEvent.TogglePin -> togglePin(event.value)
            is AddNoteEvent.ToggleReminders -> toggleReminders(event.value)
            is AddNoteEvent.ChangeReminder -> changeReminderTime(event.value)
            is AddNoteEvent.CheckListItemCheckedChange ->
                onCheckedChange(event.checkListItem, event.checked)
            AddNoteEvent.Save -> save()
        }
    }

    private fun addNoteCheckList(checkListItem: CheckListItem) {
        _state.update {
            it.copy(
                checkListItems =
                    it.checkListItems + checkListItem.copy(checkListItemId = it.checkListItems.size.toLong())
            )
        }
    }

    private fun onCheckedChange(checkListItem: CheckListItem, checked: Boolean) {
        _state.update { prevState ->
            val index =
                prevState.checkListItems.indexOfFirst {
                    it.checkListItemId == checkListItem.checkListItemId
                }
            if (index != -1) {
                val updatedItems = prevState.checkListItems.toMutableList() // Convert to mutable list
                updatedItems[index] = updatedItems[index].copy(isChecked = checked)
                prevState.copy(checkListItems = updatedItems)
            } else {
                prevState // No changes if item not found
            }
        }
    }

    private fun removeCheckListItem(checkListItem: CheckListItem) {
        _state.update {
            it.copy(
                checkListItems = it.checkListItems - checkListItem,
                note = it.note.copy(isCheckList = (it.checkListItems - checkListItem).isNotEmpty())
            )
        }
    }

    private fun toggleReminders(value: Boolean) {
        _state.update { it.copy(isReminderDialogVisible = value) }
    }

    private fun changeReminderTime(value: Long?) {
        _state.update { it.copy(note = it.note.copy(reminderTime = value)) }
    }

    private fun changeNoteText(value: String) {
        _state.update { it.copy(note = it.note.copy(noteText = value)) }
    }

    private fun togglePin(value: Boolean) {
        _state.update { it.copy(it.note.copy(isPinned = value)) }
    }

    private fun toggleColorBar(value: Boolean) {
        _state.update { it.copy(isColorVisible = value) }
    }

    private fun toggleNoteTags(value: Boolean) {
        _state.update { it.copy(hasTags = value) }
    }

    private fun toggleNoteCheckList(value: Boolean) {
        _state.update { it.copy(note = it.note.copy(isCheckList = value)) }
    }

    private fun addNoteTag(tag: Tag) {
        _state.update { it.copy(tags = it.tags + tag) }
    }

    private fun removeNoteTag(tag: Tag) {
        _state.update { it.copy(tags = it.tags - tag) }
    }

    private fun changeNoteColor(color: Color) {
        _state.update { it.copy(selectedColor = color) }
    }

    private fun save() {}

    private fun changeNoteTitle(value: String) {
        _state.update { it.copy(note = it.note.copy(noteTitle = value)) }
    }
}
