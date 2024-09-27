
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
package com.gitsoft.thoughtpad.ui.noteslist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.gitsoft.thoughtpad.model.Note
import com.gitsoft.thoughtpad.repository.NotesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NoteListViewModel(application: Application, val repository: NotesRepository) :
    AndroidViewModel(application) {

    val allNotes = repository.allNotes

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _navigateToAddNote = MutableLiveData<Boolean>()
    val navigateToAddNote: LiveData<Boolean> = _navigateToAddNote

    val empty: LiveData<Boolean> by lazy { allNotes.map { it.isEmpty() } }

    private val _navigateToViewNote = MutableLiveData<Note?>()
    val navigateToViewNote: LiveData<Note?> = _navigateToViewNote

    fun onClearAll() {
        coroutineScope.launch { repository.clear() }
    }

    fun onAddNewNote() {
        _navigateToAddNote.value = true
    }

    fun onNavigatedToAddNote() {
        _navigateToAddNote.value = false
    }

    fun openNote(note: Note) {
        _navigateToViewNote.value = note
    }

    fun onNavigateToViewNote() {
        _navigateToViewNote.value = null
    }

    fun searchDatabase(query: String): LiveData<List<Note>> {
        return repository.searchDatabase(query)
    }

    override fun onCleared() {
        viewModelJob.cancel()
        super.onCleared()
    }
}
