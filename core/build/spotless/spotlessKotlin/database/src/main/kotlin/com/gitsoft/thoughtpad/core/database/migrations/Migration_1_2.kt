
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
package com.gitsoft.thoughtpad.core.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val Migration_1_2 =
    object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // Create a new table with updated schema
            db.execSQL(
                """
                                                                                                                                                                                                                                                                                                                                                                                                CREATE TABLE new_notes_table (
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                noteId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                noteTitle TEXT DEFAULT NULL,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                noteText TEXT DEFAULT NULL,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                createdAt INTEGER DEFAULT NULL,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                updatedAt INTEGER DEFAULT NULL,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                isPinned INTEGER NOT NULL DEFAULT 0,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                isArchived INTEGER NOT NULL DEFAULT 0,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                color INTEGER DEFAULT 4294967295,  -- Default color value
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                isFavorite INTEGER NOT NULL DEFAULT 0,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                isDeleted INTEGER NOT NULL DEFAULT 0,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                isCheckList INTEGER NOT NULL DEFAULT 0,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                reminderTime INTEGER DEFAULT NULL,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                attachments TEXT NOT NULL DEFAULT ''  -- Non-null with default empty string
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                """
                    .trimIndent()
            )

            // Insert into the new table
            db.execSQL(
                """
                                                                                                                                                                                                                                                                                                                                                                                                INSERT INTO new_notes_table (
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                noteId, noteTitle, noteText, createdAt, updatedAt, isPinned, isArchived,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                color, isFavorite, isDeleted, isCheckList, reminderTime, attachments
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                                SELECT noteId, noteTitle, noteText, NULL AS createdAt, NULL AS updatedAt,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                0 AS isPinned, 0 AS isArchived,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                4294967295 AS color, 0 AS isFavorite, 0 AS isDeleted,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                0 AS isCheckList, NULL AS reminderTime,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                '' AS attachments  -- Ensure non-null attachments
                                                                                                                                                                                                                                                                                                                                                                                                FROM notes_table
                                                                                                """
                    .trimIndent()
            )

            // Drop the old table
            db.execSQL("DROP TABLE notes_table")

            // Rename the new table
            db.execSQL("ALTER TABLE new_notes_table RENAME TO notes_table")

            // Migration for noteTags
            db.execSQL(
                """
                                                                                                                                                                                                                                                                                                                                                                                                CREATE TABLE noteTags (
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                tagId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                                                                                                                                name TEXT,
                                                                                                                                color INTEGER
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                """
                    .trimIndent()
            )

            // Create index for noteTags
            db.execSQL(
                """
                                                                                                                                                                                                                                                                                                                                                                                                CREATE INDEX index_noteTags_noteId ON noteTags(noteId)
                                                                                                                                                                                                                                                                                                                                                                                                """
            )

            // Create the checklist table
            db.execSQL(
                """
                                                                                                                                                                                                                                                                                                                                                                                                CREATE TABLE checklist (
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                checkListItemId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                noteId INTEGER DEFAULT NULL,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                text TEXT DEFAULT NULL,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                isChecked INTEGER NOT NULL DEFAULT 0,  -- Change default to 1 if necessary
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                FOREIGN KEY(noteId) REFERENCES notes_table(noteId) ON DELETE CASCADE
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                """
                    .trimIndent()
            )

            // Create an index for checklist
            db.execSQL(
                """
                                                                                                                                                                                                                                                                                                                                                                                                CREATE INDEX index_checklist_noteId ON checklist(noteId)
                                                                                                                                                                                                                                                                                                                                                                                                """
            )

            // Create the cross-reference table for notes and tags
            db.execSQL(
                """
                                                                                                CREATE TABLE IF NOT EXISTS `NoteTagCrossRef` (
                                                                                                                                `noteId` INTEGER NOT NULL,
                                                                                                                                `tagId` INTEGER NOT NULL,
                                                                                                                                PRIMARY KEY(`noteId`, `tagId`),
                                                                                                                                FOREIGN KEY(`noteId`) REFERENCES `notes_table`(`noteId`) ON DELETE CASCADE,
                                                                                                                                FOREIGN KEY(`tagId`) REFERENCES `noteTags`(`tagId`) ON DELETE CASCADE
                                                                                                )
                                                                                                """
            )
        }
    }
