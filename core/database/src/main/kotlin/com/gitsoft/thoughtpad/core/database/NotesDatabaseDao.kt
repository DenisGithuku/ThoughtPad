
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

import android.util.Log
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
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDatabaseDao {

    @Transaction
    @Query("SELECT * FROM notes_table order by noteId desc")
    fun loadAllNotes(): Flow<List<DataWithNotesCheckListItemsAndTags>>

    @Transaction
    @Query("SELECT * FROM notes_table where noteId = :id")
    suspend fun noteDataById(id: Long): DataWithNotesCheckListItemsAndTags

    // Insert operations
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertNote(note: Note): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChecklistItems(checklistItems: List<CheckListItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertTags(tags: List<Tag>)

    // Update operations
    @Update suspend fun updateNote(note: Note): Int

    @Query("SELECT * FROM notes_table WHERE noteId = :id") suspend fun getNoteById(id: Long): Note

    @Update suspend fun updateChecklistItem(checklistItem: CheckListItem)

    @Update suspend fun updateTags(tags: List<Tag>): Int

    // Delete operations if needed
    @Delete suspend fun deleteChecklistItems(checklistItems: List<CheckListItem>)

    @Delete suspend fun deleteTags(tags: List<Tag>)

    @Query("SELECT * FROM noteTags") fun getTags(): Flow<List<Tag>>

    @Update suspend fun updateTag(tag: Tag): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertTag(tag: Tag): Long

    @Query("SELECT * FROM noteTags WHERE tagId = :id") suspend fun getTag(id: Long): Tag

    /**
     * Insert a note with its associated checklist items and tags.
     *
     * @param Note The note to be inserted.
     * @param checklistItems The checklist items associated with the note.
     * @param tags The tags associated with the note.
     */
    @Transaction
    suspend fun insertNoteWithDetails(
        note: Note,
        checklistItems: List<CheckListItem>,
        tags: List<Tag>
    ): Long {
        // Insert the note and get the generated noteId
        val noteId = insertNote(note)

        // Update the checklist items with the new noteId
        // Create a new checklist item to make sure the id is the default
        val updatedCheckListItems =
            checklistItems.map {
                CheckListItem(noteId = noteId, text = it.text, isChecked = it.isChecked)
            }

        Log.d("NotesDatabaseDao", "Checklist items: $updatedCheckListItems")
        insertChecklistItems(updatedCheckListItems)

        // Now insert the relation between the note and the tags in the cross-reference table
        val noteTagCrossRefs = tags.map { tag -> NoteTagCrossRef(noteId = noteId, tagId = tag.tagId) }
        insertNoteTagCrossRefs(noteTagCrossRefs)

        return noteId
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNoteTagCrossRefs(crossRefs: List<NoteTagCrossRef>)

    /**
     * Update a note with its associated checklist items and tags.
     *
     * @param Note The note to be updated.
     * @param checklistItems The checklist items associated with the note.
     * @param tags The tags associated with the note.
     */
    @Transaction
    suspend fun updateNoteWithDetails(
        note: Note,
        checklistItems: List<CheckListItem>,
        tags: List<Tag>
    ): Int {
        // Update the note in the database
        val id = updateNote(note)

        // Delete old checklist items and tags associated with the note
        deleteChecklistItems(getCheckListForNote(note.noteId))
        // Assuming getTagsForNoteId method is not needed anymore as it just retrieves and does not
        // modify the tags

        // Clear existing associations in the junction table
        deleteNoteTagCrossRefsForNoteId(note.noteId)

        // Insert the new checklist items
        val updatedCheckListItems =
            checklistItems.map {
                CheckListItem(noteId = note.noteId, text = it.text, isChecked = it.isChecked)
            }

        // Insert the new checklist items
        insertChecklistItems(updatedCheckListItems)

        // Insert new associations for the note and its tags
        tags.forEach { tag ->
            // Create a new cross-reference for each tag associated with the note
            insertNoteTagCrossRef(NoteTagCrossRef(note.noteId, tag.tagId))
        }
        return id
    }

    // Method to delete existing tag associations for the note
    @Query("DELETE FROM NoteTagCrossRef WHERE noteId = :noteId")
    suspend fun deleteNoteTagCrossRefsForNoteId(noteId: Long)

    @Query("SELECT * FROM checklist WHERE noteId = :noteId")
    fun getChecklistItemsForNoteId(noteId: Long): Flow<List<CheckListItem>>

    @Query("SELECT * FROM checklist WHERE noteId = :noteId")
    fun getCheckListForNote(noteId: Long): List<CheckListItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNoteTagCrossRef(noteTagCrossRef: NoteTagCrossRef)

    @Transaction
    suspend fun deleteNoteWithDetails(noteId: Long): Int {
        // Step 1: Delete checklist items associated with the note
        deleteChecklistItemsByNoteId(noteId)

        // Step 2: Delete note-tag cross-references for the note
        deleteNoteTagCrossRefsByNoteId(noteId)

        // Step 3: Finally, delete the note itself
        return deleteNoteById(noteId)
    }

    // Deleting checklist items by noteId
    @Query("DELETE FROM checklist WHERE noteId = :noteId")
    suspend fun deleteChecklistItemsByNoteId(noteId: Long)

    // Deleting cross-references between note and tags by noteId
    @Query("DELETE FROM NoteTagCrossRef WHERE noteId = :noteId")
    suspend fun deleteNoteTagCrossRefsByNoteId(noteId: Long)

    // Deleting the note by its noteId
    @Query("DELETE FROM notes_table WHERE noteId = :noteId")
    suspend fun deleteNoteById(noteId: Long): Int
}
