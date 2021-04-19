package com.gitsoft.notesapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NotesDatabaseDao {
    @Insert
    fun insert(note: Note)

    @Update
    fun update(note: Note)

    @Query("select * from notes_table order by noteId desc")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("select * from notes_table order by noteId desc limit 1")
    fun getOneNote(): Note?

    @Query("delete from notes_table")
    fun clear()
}