package com.gitsoft.notesapp.ui.display

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gitsoft.notesapp.database.NotesDatabaseDao

class NoteDisplayViewModelFactory(
    private val notesDatabaseDao: NotesDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("unchecked_cast")
        if (modelClass.isAssignableFrom(NoteDisplayViewModel::class.java)) {
            return NoteDisplayViewModel(notesDatabaseDao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}