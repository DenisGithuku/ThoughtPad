package com.gitsoft.thoughtpad.ui.noteslist

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gitsoft.thoughtpad.repository.NotesRepository

class NoteListViewModelFactory(
    private val application: Application,
    private val repository: NotesRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("unchecked_cast")
        if (modelClass.isAssignableFrom(NoteListViewModel::class.java) ){
            return NoteListViewModel(application, repository) as T
            }
    throw IllegalArgumentException("Unknown ViewModel class")
    }
}