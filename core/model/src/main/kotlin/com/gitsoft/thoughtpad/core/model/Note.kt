
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

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.TypeConverter
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "notes_table")
data class Note(
    @PrimaryKey(autoGenerate = true) val noteId: Long = 0,
    val noteTitle: String? = null,
    val noteText: String? = null,
    val createdAt: Long? = null,
    val updatedAt: Long? = null,
    val isPinned: Boolean = false,
    val isArchived: Boolean = false,
    val color: NoteColor = NoteColor.Default,
    val isDeleted: Boolean = false,
    val isCheckList: Boolean = false,
    val reminderTime: Long? = null,
    val attachments: List<String> = emptyList(),
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val password: ByteArray? = null
)

class AttachmentTypeConverter {

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

class NoteColorConverter {
    @TypeConverter
    fun fromNoteColor(noteColor: NoteColor): String {
        return noteColor.name
    }

    @TypeConverter
    fun toNoteColor(noteColorName: String): NoteColor {
        return NoteColor.valueOf(noteColorName)
    }
}

@Entity(
    primaryKeys = ["noteId", "tagId"],
    foreignKeys =
        [
            ForeignKey(
                entity = Note::class,
                parentColumns = ["noteId"],
                childColumns = ["noteId"],
                onDelete = ForeignKey.CASCADE
            ),
            ForeignKey(
                entity = Tag::class,
                parentColumns = ["tagId"],
                childColumns = ["tagId"],
                onDelete = ForeignKey.CASCADE
            )
        ],
    indices = [Index(value = ["tagId"])]
)
data class NoteTagCrossRef(val noteId: Long, val tagId: Long)

@Serializable
data class DataWithNotesCheckListItemsAndTags(
    @Embedded val note: Note,
    @Relation(parentColumn = "noteId", entityColumn = "noteId")
    val checkListItems: List<CheckListItem> = emptyList(),
    @Relation(
        parentColumn = "noteId",
        entityColumn = "tagId",
        associateBy = Junction(NoteTagCrossRef::class)
    )
    val tags: List<Tag> = emptyList()
)
