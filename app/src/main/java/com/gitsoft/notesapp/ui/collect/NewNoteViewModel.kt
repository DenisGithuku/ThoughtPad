package com.gitsoft.notesapp.ui.collect

import android.app.Application
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gitsoft.notesapp.database.Note
import com.gitsoft.notesapp.database.NotesDatabaseDao
import kotlinx.coroutines.*

class NewNoteViewModel(
    private val notesDatabaseDao: NotesDatabaseDao,
    application: Application
) : AndroidViewModel(application), Observable {

    private val noteData = MutableLiveData<Note?>()

    @Bindable
    val inputTitle = MutableLiveData<String>()


    @Bindable
    val inputText = MutableLiveData<String>()


    private val _noteTitle = MutableLiveData<String>()
    val noteTitle: LiveData<String>
        get() = _noteTitle

    private val _noteText = MutableLiveData<String>()
    val noteText: LiveData<String>
        get() = _noteText

    private val _navigateToDisplayFragment = MutableLiveData<Boolean>()
    val navigateToDisplayFragment: LiveData<Boolean>
        get() = _navigateToDisplayFragment

    private val _showEmptySnackBarEvent = MutableLiveData<Boolean>()
    val showEmptySnackBarEvent: LiveData<Boolean>
        get() = _showEmptySnackBarEvent

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        initializeNote()
        _navigateToDisplayFragment.value = false
        _showEmptySnackBarEvent.value = false
    }

    private fun initializeNote() {
        coroutineScope.launch {
            noteData.value = getOneNoteFromDb()
        }
    }

    private suspend fun getOneNoteFromDb(): Note? {
        return withContext(Dispatchers.IO) {
            notesDatabaseDao.getOneNote()
        }
    }

    fun onSaveNote() {
        coroutineScope.launch {
            if (inputTitle.value.isNullOrEmpty() && inputText.value.isNullOrEmpty()) {
                _showEmptySnackBarEvent.value = true
                _navigateToDisplayFragment.value = true
            } else if ((inputTitle.value?.isEmpty() == false) && inputText.value.isNullOrEmpty()) {
                _noteTitle.value = inputTitle.value
                _noteText.value = ""

                val note = Note(0, _noteTitle.value.toString(), _noteText.value.toString())
                insert(note)

                noteData.value = getOneNoteFromDb()
                _navigateToDisplayFragment.value = true
            } else if (inputTitle.value.isNullOrEmpty() && (inputText.value?.isEmpty() == false)) {
                _noteTitle.value = ""
                _noteText.value = inputText.value

                val note = Note(0, _noteTitle.value.toString(), _noteText.value.toString())
                insert(note)

                noteData.value = getOneNoteFromDb()
                _navigateToDisplayFragment.value = true
            } else {
                _noteTitle.value = inputTitle.value
                _noteText.value = inputText.value

                val note = Note(0, _noteTitle.value.toString(), _noteText.value.toString())
                insert(note)

                noteData.value = getOneNoteFromDb()
                _navigateToDisplayFragment.value = true

            }
        }
    }

    private suspend fun insert(note: Note) {
        return withContext(Dispatchers.IO) {
            notesDatabaseDao.insert(note)
        }

    }

    fun onNavigatedToDisplayFragment() {
        _navigateToDisplayFragment.value = false
    }

    fun onEmptyNote() {
        _showEmptySnackBarEvent.value = false
        _navigateToDisplayFragment.value = false
    }

    fun onNoteSaved() {
        _navigateToDisplayFragment.value = false
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun onCleared() {
        viewModelJob.cancel()
        super.onCleared()
    }
}