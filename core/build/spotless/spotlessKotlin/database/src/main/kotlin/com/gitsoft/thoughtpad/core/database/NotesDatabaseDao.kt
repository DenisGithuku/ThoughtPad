
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
import com.gitsoft.thoughtpad.core.model.NoteTagCrossRef
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

    @Query("SELECT * FROM noteTags") suspend fun getTags(): List<Tag>

    @Update suspend fun updateTag(tag: Tag)

    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertTag(tag: Tag): Long

    @Query("SELECT * FROM noteTags WHERE tagId = :id") suspend fun getTag(id: Long): Tag

    // Example transaction for inserting all data (note + checklist items + tags)
    @Transaction
    suspend fun insertNoteWithDetails(
        note: Note,
        checklistItems: List<CheckListItem>,
        tags: List<Tag>
    ) {
        // Insert the note and get the generated noteId
        val noteId = insertNote(note)

        // Update the checklist items with the new noteId
        val updatedCheckListItems = checklistItems.map { it.copy(noteId = noteId) }
        insertChecklistItems(updatedCheckListItems)

        // Insert the tags (this step ensures tags are in the tags_table)
        insertTags(tags)

        // Now insert the relation between the note and the tags in the cross-reference table
        val noteTagCrossRefs = tags.map { tag -> NoteTagCrossRef(noteId = noteId, tagId = tag.tagId) }
        insertNoteTagCrossRefs(noteTagCrossRefs)
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNoteTagCrossRefs(crossRefs: List<NoteTagCrossRef>)

    // Example transaction for updating all data (note + checklist items + tags)
    @Transaction
    suspend fun updateNoteWithDetails(
        note: Note,
        checklistItems: List<CheckListItem>,
        tags: List<Tag>
    ) {
        // Update the note in the database
        updateNote(note)

        // Delete old checklist items and tags associated with the note
        deleteChecklistItems(getChecklistItemsForNoteId(note.noteId))
        // Assuming getTagsForNoteId method is not needed anymore as it just retrieves and does not
        // modify the tags

        // Clear existing associations in the junction table
        deleteNoteTagCrossRefsForNoteId(note.noteId)

        // Insert the new checklist items
        insertChecklistItems(checklistItems)

        // Insert the new tags
        insertTags(tags)

        // Insert new associations for the note and its tags
        tags.forEach { tag ->
            // Create a new cross-reference for each tag associated with the note
            insertNoteTagCrossRef(NoteTagCrossRef(note.noteId, tag.tagId))
        }
    }

    // Method to delete existing tag associations for the note
    @Query("DELETE FROM NoteTagCrossRef WHERE noteId = :noteId")
    suspend fun deleteNoteTagCrossRefsForNoteId(noteId: Long)

    @Query("SELECT * FROM checklist WHERE noteId = :noteId")
    suspend fun getChecklistItemsForNoteId(noteId: Long): List<CheckListItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNoteTagCrossRef(noteTagCrossRef: NoteTagCrossRef)
}
