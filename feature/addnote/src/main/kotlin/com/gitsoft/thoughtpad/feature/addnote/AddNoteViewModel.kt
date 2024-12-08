
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

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gitsoft.thoughtpad.core.model.CheckListItem
import com.gitsoft.thoughtpad.core.model.NoteColor
import com.gitsoft.thoughtpad.core.model.Tag
import com.gitsoft.thoughtpad.core.model.TagColor
import com.gitsoft.thoughtpad.core.model.ThemeConfig
import core.gitsoft.thoughtpad.core.data.repository.NotesRepository
import core.gitsoft.thoughtpad.core.data.repository.UserPrefsRepository
import java.util.Calendar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddNoteViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val notesRepository: NotesRepository,
    private val userPrefsRepository: UserPrefsRepository
) : ViewModel() {

    private val _state: MutableStateFlow<AddNoteUiState> = MutableStateFlow(AddNoteUiState())

    val state: StateFlow<AddNoteUiState> =
        combine(_state, notesRepository.allTags, userPrefsRepository.userPrefs) { state, tags, userPrefs
                ->
                state.copy(
                    defaultTags = tags,
                    systemInDarkMode = userPrefs.themeConfig == ThemeConfig.DARK,
                    permissionsNotificationsGranted = userPrefs.isNotificationPermissionsGranted
                )
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AddNoteUiState())

    init {
        getNoteDetails()
    }

    private fun getNoteDetails() {
        viewModelScope.launch {
            val noteId: Long? = savedStateHandle.get<Long>("noteId")
            if (noteId != null && noteId != -1L) {
                val noteData = notesRepository.getNoteWithDataById(noteId)
                _state.update {
                    it.copy(
                        note = noteData.note,
                        checkListItems = noteData.checkListItems,
                        selectedTags = noteData.tags,
                        selectedDate = noteData.note.reminderTime ?: it.selectedDate,
                        hasReminder = noteData.note.reminderTime != null,
                        hasTags = noteData.tags.isNotEmpty(),
                        selectedNoteColor = noteData.note.color,
                        isNewNote = false,
                        encryptedPassword = noteData.note.password
                    )
                }
            }
        }
    }

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
            is AddNoteEvent.ToggleDateSheet -> toggleDateSheet(event.value)
            is AddNoteEvent.ToggleReminders -> toggleReminders(event.value)
            is AddNoteEvent.CheckListItemCheckedChange ->
                onCheckedChange(event.checkListItem, event.checked)
            is AddNoteEvent.ToggleTagSheet -> toggleTagSheet(event.isVisible)
            is AddNoteEvent.ToggleTagSelection -> toggleTagSelection(event.tag)
            is AddNoteEvent.ToggleDateDialog -> toggleDateDialog(event.value)
            is AddNoteEvent.ToggleTimeDialog -> toggleTimeDialog(event.value)
            is AddNoteEvent.ChangeDate -> changeDate(event.value)
            is AddNoteEvent.ChangeTime -> changeTime(event.value)
            is AddNoteEvent.DiscardNote -> onDiscardNote()
            is AddNoteEvent.UpdateNotificationPermissions -> updatePermissionsStatus()
            is AddNoteEvent.ChangePassword -> changePassword(event.value)
            is AddNoteEvent.TogglePasswordDialog -> togglePasswordSheet(event.isVisible)
            AddNoteEvent.SecureNote -> secureNote()
            AddNoteEvent.RemovePassword -> removePassWord()
            AddNoteEvent.Save -> save()
        }
    }

    private fun updatePermissionsStatus() {
        viewModelScope.launch { userPrefsRepository.updateNotificationPermission(true) }
    }

    private fun addCheckListItem(checkListItem: CheckListItem) {
        _state.update {
            it.copy(
                checkListItems =
                    it.checkListItems +
                        checkListItem.copy(
                            checkListItemId = it.checkListItems.size.toLong(),
                            text = checkListItem.text?.trim()
                        )
            )
        }
    }

    private fun onDiscardNote() {
        viewModelScope.launch {
            if (_state.value.note.noteId != 0L) {
                notesRepository.deleteNoteById(_state.value.note.noteId)
            }
            _state.update { it.copy(deletedSuccessfully = true) }
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

    private fun toggleTimeDialog(value: Boolean) {
        _state.update { it.copy(timeDialogIsVisible = value) }
    }

    private fun changeDate(value: Long) {
        _state.value.selectedDate?.let { selectedDate ->
            val calendar =
                Calendar.getInstance().apply {
                    timeInMillis = value // Set to the new date value (should only have date)
                }

            // Retain the time and update the state with the new date
            val updatedDate =
                Calendar.getInstance()
                    .apply {
                        timeInMillis = selectedDate // Get the current selected date
                        set(Calendar.YEAR, calendar.get(Calendar.YEAR)) // Update year
                        set(Calendar.MONTH, calendar.get(Calendar.MONTH)) // Update month
                        set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)) // Update day
                    }
                    .timeInMillis

            _state.update { it.copy(selectedDate = updatedDate) }
        }
    }

    private fun changeTime(value: Long) {
        _state.value.selectedDate?.let { selectedDate ->
            val calendar =
                Calendar.getInstance().apply {
                    timeInMillis = selectedDate // Start with the current selected date
                    timeInMillis = value // Set to the new time value (which should only have time)

                    set(Calendar.SECOND, 0) // Ensure seconds are set to 0
                    set(Calendar.MILLISECOND, 0) // Ensure milliseconds are set to 0
                }

            // Retain the date and update the state with the new time
            val updatedTime =
                Calendar.getInstance()
                    .apply {
                        timeInMillis = selectedDate // Get the current selected date
                        set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY)) // Update hour
                        set(Calendar.MINUTE, calendar.get(Calendar.MINUTE)) // Update minute
                        set(Calendar.SECOND, 0) // Reset seconds
                        set(Calendar.MILLISECOND, 0) // Reset milliseconds
                    }
                    .timeInMillis

            _state.update { it.copy(selectedDate = updatedTime) }
        }
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
        val defaultReminderTime =
            Calendar.getInstance()
                .apply {
                    set(Calendar.MINUTE, get(Calendar.MINUTE) + 30)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                .timeInMillis
        _state.update {
            it.copy(hasReminder = value, selectedDate = if (!value) null else defaultReminderTime)
        }
        toggleDateSheet(value)
    }

    private fun toggleDateSheet(value: Boolean) {
        _state.update { it.copy(isDateSheetVisible = value) }
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

    private fun secureNote() {
        viewModelScope.launch {
            val password = _state.value.password?.trim()
            if (password != null) {
                val encryptedPassword =
                    withContext(Dispatchers.IO) { notesRepository.encryptPassword(password) }
                encryptedPassword?.let {
                    Log.d("Encrypted password", it.toString())
                    _state.update { state ->
                        state.copy(encryptedPassword = it, isPasswordSheetVisible = false)
                    }
                }
            }
        }
    }

    private fun addTag(tag: Tag) {
        val trimmedTag = tag.copy(name = tag.name?.trim())
        if (_state.value.defaultTags.none { it.name.equals(trimmedTag.name, true) }) {
            viewModelScope.launch {
                val tagId = async { storeTag(trimmedTag) }.await()
                _state.update { it.copy(selectedTags = it.selectedTags + trimmedTag.copy(tagId = tagId)) }
            }
        } else {
            _state.update { it.copy(selectedTags = it.selectedTags + trimmedTag) }
        }
    }

    private fun removeTag(tag: Tag) {
        _state.update { it.copy(selectedTags = it.selectedTags - tag) }
    }

    private fun changeNoteColor(color: NoteColor) {
        _state.update { it.copy(selectedNoteColor = color) }
    }

    private fun changeTagColor(color: TagColor) {
        _state.update { it.copy(selectedTagColor = color) }
    }

    private fun changePassword(value: String?) {
        _state.update { it.copy(password = value) }
    }

    private fun togglePasswordSheet(isVisible: Boolean) {
        _state.update {
            it.copy(isPasswordSheetVisible = isVisible, password = null, encryptedPassword = null)
        }
    }

    private fun removePassWord() {
        _state.update { it.copy(password = null, encryptedPassword = null) }
    }

    private fun update() {
        viewModelScope.launch {
            val hasChanged = noteHasChanged()
            if (hasChanged) {
                notesRepository.updateNoteWithDetails(
                    note =
                        _state.value.note.copy(
                            updatedAt = Calendar.getInstance().timeInMillis,
                            color = _state.value.selectedNoteColor,
                            reminderTime = if (_state.value.hasReminder) _state.value.selectedDate else null,
                            noteTitle = _state.value.note.noteTitle?.trim(),
                            noteText = _state.value.note.noteText?.trim(),
                            password = _state.value.encryptedPassword
                        ),
                    checklistItems = _state.value.checkListItems,
                    tags = _state.value.selectedTags
                )
            }
            _state.update { it.copy(insertionSuccessful = true) }
        }
    }

    private fun save() {
        if (_state.value.isNewNote) {
            insert()
        } else {
            update()
        }
    }

    private fun insert() {
        viewModelScope.launch {
            val timeMillis = Calendar.getInstance().timeInMillis
            val note =
                _state.value.note.copy(
                    createdAt = timeMillis,
                    updatedAt = timeMillis,
                    color = _state.value.selectedNoteColor,
                    reminderTime = if (_state.value.hasReminder) _state.value.selectedDate else null,
                    noteTitle = _state.value.note.noteTitle?.trim(),
                    noteText = _state.value.note.noteText?.trim(),
                    password = _state.value.encryptedPassword
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

    private suspend fun noteHasChanged(): Boolean {
        val noteData = notesRepository.getNoteWithDataById(_state.value.note.noteId)
        return noteData != _state.value.note ||
            _state.value.checkListItems != noteData.checkListItems ||
            _state.value.selectedTags != noteData.tags ||
            _state.value.selectedNoteColor != noteData.note.color ||
            _state.value.selectedDate != noteData.note.reminderTime ||
            _state.value.hasTags != noteData.tags.isNotEmpty() ||
            _state.value.hasReminder != (noteData.note.reminderTime != null)
    }

    override fun onCleared() {
        super.onCleared()
        _state.update { AddNoteUiState() }
    }
}
