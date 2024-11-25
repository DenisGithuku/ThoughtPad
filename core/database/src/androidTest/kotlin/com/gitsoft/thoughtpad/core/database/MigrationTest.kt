
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

import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.test.core.app.ApplicationProvider
import com.gitsoft.thoughtpad.core.database.migrations.Migration_1_2
import com.gitsoft.thoughtpad.core.database.migrations.Migration_2_3
import junit.framework.TestCase.assertTrue
import org.junit.Test

class MigrationTest {

    private lateinit var database: SupportSQLiteDatabase

    @Test
    fun testMigrationFrom1To2() {
        // Create a Room database with the old version
        database =
            Room.inMemoryDatabaseBuilder(
                    ApplicationProvider.getApplicationContext(),
                    NotesDatabase::class.java
                )
                .addMigrations(Migration_1_2)
                .build()
                .openHelper
                .writableDatabase

        // Verify the new schema is correct
        database.query("PRAGMA table_info(notes_table)").use { cursor ->
            val columns = mutableListOf<String>()
            while (cursor.moveToNext()) {
                columns.add(cursor.getString(1))
            }
            assertTrue(
                columns.containsAll(listOf("noteId", "noteTitle", "noteText", "color", "attachments"))
            )
        }
    }

    @Test
    fun testMigration2To3() {
        // Create a Room database with the old version
        database =
            Room.inMemoryDatabaseBuilder(
                    ApplicationProvider.getApplicationContext(),
                    NotesDatabase::class.java
                )
                .addMigrations(Migration_1_2) // Previous migration
                .addMigrations(Migration_2_3) // Migration under test
                .build()
                .openHelper
                .writableDatabase

        // Verify the new schema is correct
        database.query("PRAGMA table_info(notes_table)").use { cursor ->
            val columns = mutableListOf<String>()
            while (cursor.moveToNext()) {
                columns.add(cursor.getString(1))
            }
            assertTrue(
                columns.contains("password") // Expected column after migration
            )
        }
    }
}
