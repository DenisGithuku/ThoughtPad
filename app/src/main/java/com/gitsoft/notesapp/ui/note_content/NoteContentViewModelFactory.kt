package com.gitsoft.notesapp.ui.note_content

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gitsoft.notesapp.database.Note
import java.lang.IllegalArgumentException

class NoteContentViewModelFactory(
    val note: Note,
    val application: Application
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("unchecked_cast")
        if (modelClass.isAssignableFrom(NoteContentViewModel::class.java)) {
            return NoteContentViewModel(note, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}