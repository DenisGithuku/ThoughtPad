package com.gitsoft.notesapp.ui.collect

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gitsoft.notesapp.database.NotesDatabaseDao

class NewNoteViewModelFactory(
    private val notesDatabaseDao: NotesDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("unchecked_cast")
        if (modelClass.isAssignableFrom(NewNoteViewModel::class.java)) {
            return NewNoteViewModel(notesDatabaseDao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}