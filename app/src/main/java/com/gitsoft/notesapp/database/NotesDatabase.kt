package com.gitsoft.notesapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gitsoft.notesapp.model.Note

@Database(entities = [Note::class], version = 2, exportSchema = false)
abstract class NotesDatabase: RoomDatabase() {
    abstract val dao: NotesDatabaseDao

    companion object {
        @Volatile

        private var INSTANCE: NotesDatabase? = null
        fun getInstance(context: Context): NotesDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        NotesDatabase::class.java,
                        "notes_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }

    }
}