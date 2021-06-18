package com.gitsoft.notesapp.ui.viewnote

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gitsoft.notesapp.model.Note
import com.gitsoft.notesapp.repository.NotesRepository
import kotlinx.coroutines.*

class ViewNoteViewModel(
    val repository: NotesRepository,
    val note: Note,
    application: Application
) : AndroidViewModel(application) {


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

    private suspend fun insert(note: Note){
        return withContext(Dispatchers.IO){
            repository.insert(note)
        }
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
        return withContext(Dispatchers.IO) {
            repository.delete(note)
        }
    }

    private suspend fun update(note: Note) {
        return withContext(Dispatchers.IO) {
            repository.update(note)
        }
    }

    fun onShowDeleteNoteEvent() {
        _deleteNoteEvent.value = false
    }

    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }

}