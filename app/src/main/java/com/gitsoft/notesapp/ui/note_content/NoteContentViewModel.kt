package com.gitsoft.notesapp.ui.note_content

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gitsoft.notesapp.database.Note

class NoteContentViewModel(
    val note: Note,
    application: Application
) : AndroidViewModel(application) {

    private val _selectedNote = MutableLiveData<Note?>()
    val selectedNote: LiveData<Note?>
        get() = _selectedNote

    init {
        _selectedNote.value = note
    }
}