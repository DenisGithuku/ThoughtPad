
/*
* Copyright 2024 Denis Githuku
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* https://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
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
        return withContext(Dispatchers.IO) { dao.insert(note) }
    }

    suspend fun delete(note: Note) {
        return withContext(Dispatchers.IO) { dao.delete(note) }
    }

    suspend fun update(note: Note) {
        return withContext(Dispatchers.IO) { dao.update(note) }
    }

    suspend fun clear() {
        return withContext(Dispatchers.IO) { dao.clear() }
    }

    fun searchDatabase(query: String): LiveData<List<Note>> {
        return dao.searchDatabase(query)
    }
}
