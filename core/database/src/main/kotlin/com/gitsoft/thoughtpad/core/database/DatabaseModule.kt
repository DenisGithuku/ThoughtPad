
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

import android.app.Application
import androidx.room.Room
import com.gitsoft.thoughtpad.core.database.migrations.Migration_1_2
import com.gitsoft.thoughtpad.core.database.migrations.Migration_2_3
import org.koin.dsl.module

val databaseModule = module {
    single<NotesDatabase> {
        Room.databaseBuilder(get<Application>(), NotesDatabase::class.java, "notes_database")
            .addMigrations(Migration_1_2, Migration_2_3)
            .build()
    }

    single<NotesDatabaseDao> { get<NotesDatabase>().dao() }
}
