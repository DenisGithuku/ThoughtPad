
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
package com.gitsoft.thoughtpad.ui.addnote

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gitsoft.thoughtpad.model.Note
import com.gitsoft.thoughtpad.repository.NotesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddEditNoteViewModel(private val repository: NotesRepository, application: Application) :
    AndroidViewModel(application) {

    val title = MutableLiveData<String>()

    val text = MutableLiveData<String>()

    private val _navigateToNoteDisplay = MutableLiveData<Boolean>()
    val navigateToNoteDisplay: LiveData<Boolean> = _navigateToNoteDisplay

    private val _noteEmptyEvent = MutableLiveData<Boolean>()
    val noteEmptyEvent: LiveData<Boolean> = _noteEmptyEvent

    private val _noteAddedEvent = MutableLiveData<Boolean>()
    val noteAddedEvent: LiveData<Boolean> = _noteAddedEvent

    private val _backgroundChanged = MutableLiveData<Boolean>()
    val backgroundChanged: LiveData<Boolean> = _backgroundChanged

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    fun onSaveNote() {
        val title = title.value
        val text = text.value

        if (title.isNullOrEmpty() || text.isNullOrEmpty()) {
            _noteEmptyEvent.value = true
        } else {
            val note = Note(0, title, text)

            coroutineScope.launch { insert(note) }

            _navigateToNoteDisplay.value = true
            _noteAddedEvent.value = true
        }
    }

    fun finishedShowingSnackBar() {
        _noteEmptyEvent.value = false
        _noteAddedEvent.value = false
    }

    fun onNavigatedToNoteDisplay() {
        _navigateToNoteDisplay.value = false
    }

    private suspend fun insert(note: Note) {
        return withContext(Dispatchers.IO) { repository.insert(note) }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
