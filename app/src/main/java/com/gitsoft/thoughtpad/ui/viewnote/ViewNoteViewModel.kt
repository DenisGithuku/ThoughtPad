
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
package com.gitsoft.thoughtpad.ui.viewnote

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gitsoft.thoughtpad.model.Note
import com.gitsoft.thoughtpad.repository.NotesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewNoteViewModel(val repository: NotesRepository, val note: Note, application: Application) :
    AndroidViewModel(application) {

    private val _selectedNote = MutableLiveData<Note>()

    private val _navigateToNoteDisplay = MutableLiveData<Boolean>()
    val navigateToNoteDisplay: LiveData<Boolean> = _navigateToNoteDisplay

    private val _noteEmptyEvent = MutableLiveData<Boolean>()
    val noteEmptyEvent: LiveData<Boolean> = _noteEmptyEvent

    private val _deleteNoteEvent = MutableLiveData<Boolean>()
    val deleteNoteEvent: LiveData<Boolean> = _deleteNoteEvent

    val noteTitle = MutableLiveData<String>()

    val noteText = MutableLiveData<String>()

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        _selectedNote.value = note
        noteTitle.value = _selectedNote.value!!.noteTitle!!
        noteText.value = _selectedNote.value!!.noteText!!
    }

    fun onUpdateNote() {
        viewModelScope.launch {
            val id = _selectedNote.value!!.noteId
            val title = noteTitle.value
            val text = noteText.value

            if (title.isNullOrEmpty() || text.isNullOrEmpty()) {
                _noteEmptyEvent.value = true
            } else {
                val note = Note(id, title, text)
                update(note)
            }

            _navigateToNoteDisplay.value = true
        }
    }

    fun undoDelete() {
        coroutineScope.launch {
            val id = note.noteId
            val title = note.noteTitle
            val text = note.noteText

            val note = Note(id, title, text)

            insert(note)
        }
    }

    private suspend fun insert(note: Note) {
        return withContext(Dispatchers.IO) { repository.insert(note) }
    }

    fun onNavigatedToNoteDisplay() {
        _navigateToNoteDisplay.value = false
    }

    fun deleteNote() {
        viewModelScope.launch {
            delete(note)

            _navigateToNoteDisplay.value = true
            _deleteNoteEvent.value = true
        }
    }

    private suspend fun delete(note: Note) {
        return withContext(Dispatchers.IO) { repository.delete(note) }
    }

    private suspend fun update(note: Note) {
        return withContext(Dispatchers.IO) { repository.update(note) }
    }

    fun onShowDeleteNoteEvent() {
        _deleteNoteEvent.value = false
    }

    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }
}
