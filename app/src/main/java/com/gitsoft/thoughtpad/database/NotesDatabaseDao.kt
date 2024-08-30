package com.gitsoft.thoughtpad.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.gitsoft.thoughtpad.model.Note


@Dao
interface NotesDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("delete from notes_table")
    suspend fun clear()

    @Query("select * from notes_table order by noteId desc")
    fun loadAllNotes(): LiveData<List<Note>>

    @Query("select * from notes_table order by noteId desc limit 1")
    fun loadOneNote(): LiveData<Note?>

    @Query("select * from notes_table where noteTitle like :query or noteText like :query")
    fun searchDatabase(query: String): LiveData<List<Note>>

}