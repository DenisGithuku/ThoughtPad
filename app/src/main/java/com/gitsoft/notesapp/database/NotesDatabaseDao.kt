package com.gitsoft.notesapp.database

import androidx.room.*
import com.gitsoft.notesapp.model.Note
import kotlinx.coroutines.flow.Flow


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
    fun loadAllNotes(): Flow<List<Note>>

    @Query("select * from notes_table order by noteId desc limit 1")
    fun loadOneNote(): Flow<Note?>

    @Query("select * from notes_table where noteTitle like :query or noteText like :query")
    fun searchDatabase(query: String): Flow<List<Note>>

}