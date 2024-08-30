package com.gitsoft.thoughtpad.repository

import androidx.lifecycle.LiveData
import com.gitsoft.thoughtpad.database.NotesDatabaseDao
import com.gitsoft.thoughtpad.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NotesRepository(private val dao: NotesDatabaseDao) {


    val allNotes: LiveData<List<Note>> = dao.loadAllNotes()
    val oneNote: LiveData<Note?> = dao.loadOneNote()

    suspend fun insert(note: Note) {
        return withContext(Dispatchers.IO) {
            dao.insert(note)
        }
    }

    suspend fun delete(note: Note) {
        return withContext(Dispatchers.IO) {
            dao.delete(note)
        }
    }

    suspend fun update(note: Note) {
        return withContext(Dispatchers.IO) {
            dao.update(note)
        }
    }

    suspend fun clear() {
        return withContext(Dispatchers.IO) {
            dao.clear()
        }
    }

    fun searchDatabase(query: String): LiveData<List<Note>> {
            return dao.searchDatabase(query)
    }

}