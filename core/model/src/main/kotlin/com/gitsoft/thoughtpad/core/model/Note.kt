
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
package com.gitsoft.thoughtpad.core.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "notes_table")
data class Note(
    @PrimaryKey(autoGenerate = true) val noteId: Long,
    val noteTitle: String? = null,
    val noteText: String? = null,
    val createdAt: Long? = null,
    val updatedAt: Long? = null,
    val isPinned: Boolean = false,
    val isArchived: Boolean = false,
    val color: String? = null,
    val isFavorite: Boolean = false,
    val isDeleted: Boolean = false,
    val isCheckList: Boolean = false,
    val reminderTime: Long? = null,
    @TypeConverters(Converters::class) val attachments: List<String> = emptyList()
)

class Converters {

    @TypeConverter
    fun fromAttachmentsList(attachments: List<String>?): String {
        return attachments?.joinToString(separator = ",") ?: ""
    }

    @TypeConverter
    fun toAttachmentsList(attachmentsString: String): List<String> {
        return if (attachmentsString.isEmpty()) {
            emptyList()
        } else {
            attachmentsString.split(",")
        }
    }
}

data class DataWithNotesCheckListItemsAndTags(
    @Embedded val note: Note,
    @Relation(parentColumn = "noteId", entityColumn = "noteId")
    val checkListItems: List<CheckListItem> = emptyList(),
    @Relation(parentColumn = "noteId", entityColumn = "noteId") val tags: List<Tag> = emptyList()
)
