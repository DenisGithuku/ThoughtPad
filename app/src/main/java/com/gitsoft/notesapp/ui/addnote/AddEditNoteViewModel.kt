package com.gitsoft.notesapp.ui.addnote

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.gitsoft.notesapp.model.Note
import com.gitsoft.notesapp.repository.NotesRepository
import kotlinx.coroutines.*

class AddEditNoteViewModel(
    private val repository: NotesRepository,
    application: Application
) : AndroidViewModel(application) {


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

    val allNotes = repository.allNotes.asLiveData()
    val oneNote = repository.oneNote.asLiveData()

    init {

    }

    fun onSaveNote() {
        val title = title.value
        val text = text.value

        if (title.isNullOrEmpty() || text.isNullOrEmpty()) {
            _noteEmptyEvent.value = true
        } else {
            val note = Note(0, title, text)

            coroutineScope.launch {
                insert(note)
            }

            _navigateToNoteDisplay.value = true
            _noteAddedEvent.value = true
        }
    }

    fun onCyanClicked() {
        _backgroundChanged.value = true
    }

    fun onBlueClicked() {
        _backgroundChanged.value = true
    }
    fun onOrangeClicked() {
        _backgroundChanged.value = true
    }
    fun onGreenClicked() {
        _backgroundChanged.value = true
    }
    fun onWhiteClicked() {
        _backgroundChanged.value = true
    }
    fun onYellowClicked() {
        _backgroundChanged.value = true
    }

    fun finishedShowingSnackBar() {
        _noteEmptyEvent.value = false
        _noteAddedEvent.value = false
    }

    fun onNavigatedToNoteDisplay() {
        _navigateToNoteDisplay.value = false
    }

    private suspend fun insert(note: Note) {
        return withContext(Dispatchers.IO) {
            repository.insert(note)
        }
    }

    override fun onCleared() {
        viewModelJob.cancel()
        super.onCleared()
    }
}