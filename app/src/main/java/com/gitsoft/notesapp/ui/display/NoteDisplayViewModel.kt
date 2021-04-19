package com.gitsoft.notesapp.ui.display

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gitsoft.notesapp.database.Note
import com.gitsoft.notesapp.database.NotesDatabaseDao

class NoteDisplayViewModel(
    private val databaseDao: NotesDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private val _navigateToNewNote = MutableLiveData<Boolean>()
    val navigateToNewNote: LiveData<Boolean>
        get() = _navigateToNewNote

    private val _navigateToNoteContent = MutableLiveData<Note?>()
    val navigateToNoteContent: LiveData<Note?>
        get() = _navigateToNoteContent


    val allNotes = databaseDao.getAllNotes()

    init {
        _navigateToNewNote.value = false
    }

    fun onNavigateToNewNote() {
        _navigateToNewNote.value = true
    }

    fun onDoneNavigatingToNewNote() {
        _navigateToNewNote.value = false
    }

    fun onNavigateToNoteContent(note: Note) {
        _navigateToNoteContent.value = note
    }

    fun onDoneNavigatingToNoteContent() {
        _navigateToNoteContent.value = null
    }


}