package com.gitsoft.notesapp.ui.noteslist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.gitsoft.notesapp.model.Note
import com.gitsoft.notesapp.repository.NotesRepository
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

    val empty: LiveData<Boolean> by lazy {
        Transformations.map(allNotes) {
        it.isEmpty()
    }
    }


    private val _navigateToViewNote = MutableLiveData<Note?>()
    val navigateToViewNote: LiveData<Note?> = _navigateToViewNote



    init {

    }

    fun onClearAll() {
        coroutineScope.launch {
            repository.clear()
        }
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