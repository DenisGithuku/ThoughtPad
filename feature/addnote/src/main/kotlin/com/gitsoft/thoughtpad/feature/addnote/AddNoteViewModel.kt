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
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gitsoft.thoughtpad.core.model.CheckListItem
import com.gitsoft.thoughtpad.core.model.Tag
import com.gitsoft.thoughtpad.core.model.ThemeConfig
import core.gitsoft.thoughtpad.core.data.repository.NotesRepository
import core.gitsoft.thoughtpad.core.data.repository.UserPrefsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar

class AddNoteViewModel(
    private val notesRepository: NotesRepository, userPrefsRepository: UserPrefsRepository
) : ViewModel() {

    private val _state: MutableStateFlow<AddNoteUiState> = MutableStateFlow(AddNoteUiState())

    val state: StateFlow<AddNoteUiState> = combine(
        _state, notesRepository.allTags, userPrefsRepository.userPrefs
    ) { state, tags, userPrefs ->
        state.copy(defaultTags = tags, systemInDarkMode = userPrefs.themeConfig == ThemeConfig.DARK)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AddNoteUiState())

    fun onEvent(event: AddNoteEvent) {
        when (event) {
            is AddNoteEvent.AddCheckListItem -> addCheckListItem(event.checkListItem)
            is AddNoteEvent.AddTag -> addTag(event.tag)
            is AddNoteEvent.ChangeNoteColor -> changeNoteColor(event.value)
            is AddNoteEvent.ChangeTagColor -> changeTagColor(event.value)
            is AddNoteEvent.ChangeText -> changeText(event.value)
            is AddNoteEvent.ChangeTitle -> changeTitle(event.value)
            is AddNoteEvent.RemoveCheckListItem -> removeCheckListItem(event.checkListItem)
            is AddNoteEvent.RemoveTag -> removeTag(event.tag)
            is AddNoteEvent.ToggleCheckList -> toggleCheckList(event.value)
            is AddNoteEvent.ToggleTags -> toggleTags(event.value)
            is AddNoteEvent.ToggleColorBar -> toggleColorBar(event.value)
            is AddNoteEvent.TogglePin -> togglePin(event.value)
            is AddNoteEvent.ToggleReminders -> toggleReminders(event.value)
            is AddNoteEvent.CheckListItemCheckedChange -> onCheckedChange(
                event.checkListItem, event.checked
            )

            is AddNoteEvent.ToggleTagSheet -> toggleTagSheet(event.isVisible)
            is AddNoteEvent.ToggleTagSelection -> toggleTagSelection(event.tag)
            is AddNoteEvent.ToggleDateDialog -> toggleDateDialog(event.value)
            is AddNoteEvent.ToggleTimeDialog -> toggleTimeDialog(event.value)
            is AddNoteEvent.ChangeDate -> changeDate(event.value)
            is AddNoteEvent.ChangeTime -> changeTime(event.value)
            AddNoteEvent.Save -> save()
        }
    }

    private fun addCheckListItem(checkListItem: CheckListItem) {
        _state.update {
            it.copy(
                checkListItems = it.checkListItems + checkListItem.copy(checkListItemId = it.checkListItems.size.toLong())
            )
        }
    }

    private fun onCheckedChange(checkListItem: CheckListItem, checked: Boolean) {
        _state.update { prevState ->
            val index = prevState.checkListItems.indexOfFirst {
                it.checkListItemId == checkListItem.checkListItemId
            }
            if (index != -1) {
                val updatedItems =
                    prevState.checkListItems.toMutableList() // Convert to mutable list
                updatedItems[index] = updatedItems[index].copy(isChecked = checked)
                prevState.copy(checkListItems = updatedItems)
            } else {
                prevState // No changes if item not found
            }
        }
    }

    private fun toggleTimeDialog(value: Boolean) {
        _state.update { it.copy(timeDialogIsVisible = value) }
    }

    private fun changeDate(value: Long) {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = value // Set to the new date value (should only have date)
        }

        // Retain the time and update the state with the new date
        val updatedDate = Calendar.getInstance().apply {
            timeInMillis = _state.value.selectedDate // Get the current selected date
            set(Calendar.YEAR, calendar.get(Calendar.YEAR)) // Update year
            set(Calendar.MONTH, calendar.get(Calendar.MONTH)) // Update month
            set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)) // Update day
        }.timeInMillis

        _state.update { it.copy(selectedDate = updatedDate) }
    }

    private fun changeTime(value: Long) {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = _state.value.selectedDate // Start with the current selected date
            timeInMillis = value // Set to the new time value (which should only have time)

            set(Calendar.SECOND, 0) // Ensure seconds are set to 0
            set(Calendar.MILLISECOND, 0) // Ensure milliseconds are set to 0
        }

        // Retain the date and update the state with the new time
        val updatedTime = Calendar.getInstance().apply {
            timeInMillis = _state.value.selectedDate // Get the current selected date
            set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY)) // Update hour
            set(Calendar.MINUTE, calendar.get(Calendar.MINUTE)) // Update minute
            set(Calendar.SECOND, 0) // Reset seconds
            set(Calendar.MILLISECOND, 0) // Reset milliseconds
        }.timeInMillis

        _state.update { it.copy(selectedDate = updatedTime) }
    }

    private fun toggleDateDialog(value: Boolean) {
        _state.update { it.copy(dateDialogIsVisible = value) }
    }

    private fun toggleTagSelection(tag: Tag) {
        if (tag in _state.value.selectedTags) {
            removeTag(tag)
        } else {
            addTag(tag)
        }
    }

    private fun toggleTagSheet(isVisible: Boolean) {
        _state.update { it.copy(isTagSheetVisible = isVisible) }
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
        _state.update { it.copy(hasReminder = value) }
    }

    private fun changeText(value: String) {
        _state.update { it.copy(note = it.note.copy(noteText = value)) }
    }

    private fun togglePin(value: Boolean) {
        _state.update { it.copy(it.note.copy(isPinned = value)) }
    }

    private fun toggleColorBar(value: Boolean) {
        _state.update { it.copy(isColorVisible = value) }
    }

    private fun toggleTags(value: Boolean) {
        _state.update { it.copy(hasTags = value) }
    }

    private fun toggleCheckList(value: Boolean) {
        _state.update { it.copy(note = it.note.copy(isCheckList = value)) }
    }

    private fun addTag(tag: Tag) {
        if (_state.value.defaultTags.none { it.name.equals(tag.name, true) }) {
            viewModelScope.launch {
                val tagId = async { storeTag(tag) }.await()
                _state.update { it.copy(selectedTags = it.selectedTags + tag.copy(tagId = tagId)) }

            }
        } else {
            _state.update { it.copy(selectedTags = it.selectedTags + tag) }
        }
    }

    private fun removeTag(tag: Tag) {
        _state.update { it.copy(selectedTags = it.selectedTags - tag) }
    }

    private fun changeNoteColor(color: Color) {
        _state.update { it.copy(selectedNoteColor = color) }
    }

    private fun changeTagColor(color: Color) {
        _state.update { it.copy(selectedTagColor = color) }
    }

    private fun save() {
        viewModelScope.launch {
            val timeMillis = Calendar.getInstance().timeInMillis
            val note = _state.value.note.copy(
                createdAt = timeMillis,
                updatedAt = timeMillis,
                color = _state.value.selectedNoteColor.toArgb().toLong(),
                reminderTime = _state.value.selectedDate
            )
            notesRepository.insertNoteWithDetails(
                note = note,
                checklistItems = _state.value.checkListItems,
                tags = _state.value.selectedTags
            )
            _state.update { it.copy(insertionSuccessful = true) }
        }
    }

    private fun changeTitle(value: String) {
        _state.update { it.copy(note = it.note.copy(noteTitle = value)) }
    }

    private suspend fun storeTag(tag: Tag): Long {
        return notesRepository.insertTag(tag)
    }

    override fun onCleared() {
        super.onCleared()
        _state.update { AddNoteUiState() }
    }
}
