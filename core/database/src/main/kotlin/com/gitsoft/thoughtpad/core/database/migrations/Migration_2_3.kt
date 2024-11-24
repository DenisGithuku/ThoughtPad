package com.gitsoft.thoughtpad.core.database.migrations

import android.util.Log
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import timber.log.Timber

// spotless:off
val Migration_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        try {
            db.execSQL("ALTER TABLE notes ADD COLUMN password BLOB")
        } catch (e: Exception) {
            // Handle the exception, e.g., log it
            Timber.tag("Migration").e("Error adding password column")
        }
    }
}
// spotless:on