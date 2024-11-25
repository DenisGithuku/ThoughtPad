
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

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gitsoft.thoughtpad.core.model.AttachmentTypeConverter
import com.gitsoft.thoughtpad.core.model.CheckListItem
import com.gitsoft.thoughtpad.core.model.Note
import com.gitsoft.thoughtpad.core.model.NoteColorConverter
import com.gitsoft.thoughtpad.core.model.NoteTagCrossRef
import com.gitsoft.thoughtpad.core.model.Tag
import com.gitsoft.thoughtpad.core.model.TagColorConverter

@Database(
    entities = [Note::class, Tag::class, CheckListItem::class, NoteTagCrossRef::class],
    version = 3
)
@TypeConverters(AttachmentTypeConverter::class, NoteColorConverter::class, TagColorConverter::class)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun dao(): NotesDatabaseDao
}
