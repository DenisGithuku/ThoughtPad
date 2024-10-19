
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
package com.gitsoft.thoughtpad.core.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.gitsoft.thoughtpad.core.model.Note

@Dao
interface NotesDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(note: Note)

    @Update suspend fun update(note: Note)

    @Delete suspend fun delete(note: Note)

    @Query("delete from notes_table") suspend fun clear()

    @Query("select * from notes_table order by noteId desc") fun loadAllNotes(): List<Note>

    @Query("select * from notes_table order by noteId desc limit 1") fun loadOneNote(): Note?

    @Query("select * from notes_table where noteTitle like :query or noteText like :query")
    fun searchDatabase(query: String): List<Note>
}
