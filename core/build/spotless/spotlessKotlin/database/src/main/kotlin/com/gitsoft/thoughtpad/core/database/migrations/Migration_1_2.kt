
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
            db.execSQL(
                """
                                                ALTER TABLE notes_table ADD COLUMN createdAt INTEGER DEFAULT NULL
                                                """
                    .trimIndent()
            )
            db.execSQL(
                """
                                                ALTER TABLE notes_table ADD COLUMN updatedAt INTEGER DEFAULT NULL
                                                """
                    .trimIndent()
            )
            db.execSQL(
                """
                                                ALTER TABLE notes_table ADD COLUMN isPinned INTEGER NOT NULL DEFAULT 0
                                                """
                    .trimIndent()
            )
            db.execSQL(
                """
                                                ALTER TABLE notes_table ADD COLUMN isArchived INTEGER NOT NULL DEFAULT 0
                                                """
                    .trimIndent()
            )
            db.execSQL(
                """
                                                ALTER TABLE notes_table ADD COLUMN color TEXT DEFAULT NULL
                                                """
                    .trimIndent()
            )
            db.execSQL(
                """
                                                ALTER TABLE notes_table ADD COLUMN isFavorite INTEGER NOT NULL DEFAULT 0
                                                """
                    .trimIndent()
            )
            db.execSQL(
                """
                                                ALTER TABLE notes_table ADD COLUMN isDeleted INTEGER DEFAULT NULL
                                                """
                    .trimIndent()
            )
            db.execSQL(
                """
                                                ALTER TABLE notes_table ADD COLUMN isCheckList INTEGER NOT NULL DEFAULT 0
                                                """
                    .trimIndent()
            )
            db.execSQL(
                """
                                                ALTER TABLE notes_table ADD COLUMN reminderTime INTEGER DEFAULT NULL
                                                """
                    .trimIndent()
            )
            db.execSQL(
                """
                                                ALTER TABLE notes_table ADD COLUMN attachments TEXT DEFAULT '[]'
                                                """
                    .trimIndent()
            )
        }
    }
