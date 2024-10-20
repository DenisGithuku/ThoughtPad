
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

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.gitsoft.thoughtpad.core.model.CheckListItem
import com.gitsoft.thoughtpad.core.model.DataWithNotesCheckListItemsAndTags
import com.gitsoft.thoughtpad.core.model.Note
import com.gitsoft.thoughtpad.core.model.Tag

@Dao
interface NotesDatabaseDao {

    @Transaction
    @Query("SELECT * FROM notes_table order by noteId desc")
    suspend fun loadAllNotes(): List<DataWithNotesCheckListItemsAndTags>

    @Transaction
    @Query("SELECT * FROM notes_table where noteId = :id")
    suspend fun noteById(id: Int): DataWithNotesCheckListItemsAndTags

    // Insert operations
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertNote(note: Note): Long

    @Delete suspend fun delete(note: Note)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChecklistItems(checklistItems: List<CheckListItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertTags(tags: List<Tag>)

    // Update operations
    @Update suspend fun updateNote(note: Note)

    @Update suspend fun updateChecklistItem(checklistItem: CheckListItem)

    @Update suspend fun updateTags(tags: List<Tag>)

    // Delete operations if needed
    @Delete suspend fun deleteChecklistItems(checklistItems: List<CheckListItem>)

    @Delete suspend fun deleteTags(tags: List<Tag>)

    // Example transaction for inserting all data (note + checklist items + tags)
    @Transaction
    suspend fun insertNoteWithDetails(
        note: Note,
        checklistItems: List<CheckListItem>,
        tags: List<Tag>
    ) {
        val noteId = insertNote(note)
        val updatedCheckListItems = checklistItems.map { it.copy(noteId = noteId) } // Set foreign key
        val updatedTagList = tags.map { it.copy(noteId = noteId) } // Set foreign key

        insertChecklistItems(updatedCheckListItems)
        insertTags(updatedTagList)
    }

    // Example transaction for updating all data (note + checklist items + tags)
    @Transaction
    suspend fun updateNoteWithDetails(
        note: Note,
        checklistItems: List<CheckListItem>,
        tags: List<Tag>
    ) {
        updateNote(note)

        // We want to delete old checklist items and tags before adding new ones
        deleteChecklistItems(getChecklistItemsForNoteId(note.noteId))
        deleteTags(getTagsForNoteId(note.noteId))

        insertChecklistItems(checklistItems)
        insertTags(tags)
    }

    @Query("SELECT * FROM checklist WHERE noteId = :noteId")
    suspend fun getChecklistItemsForNoteId(noteId: Long): List<CheckListItem>

    @Query("SELECT * FROM noteTags WHERE noteId = :noteId")
    suspend fun getTagsForNoteId(noteId: Long): List<Tag>
}
