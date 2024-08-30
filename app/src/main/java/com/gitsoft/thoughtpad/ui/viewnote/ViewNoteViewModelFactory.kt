package com.gitsoft.thoughtpad.ui.viewnote

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gitsoft.thoughtpad.model.Note
import com.gitsoft.thoughtpad.repository.NotesRepository

class ViewNoteViewModelFactory(
    private val repository: NotesRepository,
    private val note: Note,
    private val application: Application
    ): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("unchecked_cast")
        if (modelClass.isAssignableFrom(ViewNoteViewModel::class.java)) {
            return ViewNoteViewModel(repository, note, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}