package com.gitsoft.thoughtpad.ui.addnote

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gitsoft.thoughtpad.repository.NotesRepository
import java.lang.IllegalArgumentException

class AddEditNoteViewModelFactory(
    private val application: Application,
    private val repository: NotesRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("unchecked_cast")
        if (modelClass.isAssignableFrom(AddEditNoteViewModel::class.java)) {
            return AddEditNoteViewModel(repository, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}