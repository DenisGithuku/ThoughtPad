
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
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import com.gitsoft.thoughtpad.core.model.CheckListItem
import com.gitsoft.thoughtpad.core.model.Note
import com.gitsoft.thoughtpad.core.model.NoteColor
import com.gitsoft.thoughtpad.core.model.Tag
import com.gitsoft.thoughtpad.core.model.TagColor
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@SmallTest
class NotesDatabaseTest {

    private lateinit var notesDatabase: NotesDatabase
    private lateinit var notesDatabaseDao: NotesDatabaseDao

    @Before
    fun openDb() {
        notesDatabase =
            Room.inMemoryDatabaseBuilder(
                    ApplicationProvider.getApplicationContext(),
                    NotesDatabase::class.java
                )
                .enableMultiInstanceInvalidation()
                .allowMainThreadQueries()
                .build()

        notesDatabaseDao = notesDatabase.dao()
    }

    @After
    fun closeDb() {
        notesDatabase.close()
    }

    @Test
    fun testInsertTag() = runTest {
        val result = notesDatabaseDao.insertTag(TestData.tag)
        assertThat(result).isEqualTo(1)
    }

    @Test
    fun testGetTag() = runTest {
        notesDatabaseDao.insertTag(TestData.tag)
        val result = notesDatabaseDao.getTag(1)
        assertThat(result).isEqualTo(TestData.tag)
    }

    @Test
    fun testInsertNoteWithDetails() = runTest {
        notesDatabaseDao.insertTags(TestData.tags)

        val result =
            notesDatabaseDao.insertNoteWithDetails(
                note = TestData.note,
                checklistItems = TestData.checklistItems,
                tags = TestData.tags
            )

        assertThat(result).isEqualTo(1)
    }

    @Test
    fun testUpdateNoteWithDetails() = runTest {
        notesDatabaseDao.insertTags(TestData.tags)

        notesDatabaseDao.insertNoteWithDetails(
            note = TestData.note,
            checklistItems = TestData.checklistItems,
            tags = TestData.tags
        )

        val noteData = notesDatabaseDao.loadAllNotes().first().first()

        notesDatabaseDao.updateNoteWithDetails(
            note = noteData.note.copy(noteTitle = "Updated Title", noteText = "Updated Text"),
            checklistItems = TestData.checklistItems.map { it.copy(isChecked = true) },
            tags = TestData.tags.map { it.copy(name = "Updated Tag") }
        )

        val updatedNoteData = notesDatabaseDao.loadAllNotes().first().first()

        assertThat(updatedNoteData.note.noteTitle).isEqualTo("Updated Title")

        assertThat(updatedNoteData.checkListItems.all { it.isChecked }).isTrue()
    }

    @Test
    fun testDeleteNoteWithDetails() = runTest {
        notesDatabaseDao.insertTags(TestData.tags)
        notesDatabaseDao.insertNoteWithDetails(
            note = TestData.note,
            checklistItems = TestData.checklistItems,
            tags = TestData.tags
        )
        val result = notesDatabaseDao.deleteNoteWithDetails(0)
        assertThat(result).isEqualTo(0)
    }

    @Test
    fun testDeleteTagWithNoteAssociation() = runTest {
        notesDatabaseDao.insertTags(TestData.tags)
        notesDatabaseDao.insertNoteWithDetails(
            note = TestData.note,
            checklistItems = TestData.checklistItems,
            tags = TestData.tags
        )
        val result = notesDatabaseDao.deleteTagWithNoteAssociation(TestData.tags[0])
        assertThat(result).isEqualTo(1)
    }

    @Test
    fun testDeleteTag() = runTest {
        notesDatabaseDao.insertTags(TestData.tags)
        val result = notesDatabaseDao.deleteTag(TestData.tags[0])
        assertThat(result).isEqualTo(1)
    }

    @Test
    fun testUpdateTag() = runTest {
        notesDatabaseDao.insertTags(TestData.tags)
        val result = notesDatabaseDao.updateTag(TestData.tags[0].copy(name = "Updated Tag"))
        assertThat(result).isEqualTo(1)
    }

    @Test
    fun testGetTags() = runTest {
        notesDatabaseDao.insertTags(TestData.tags)
        val result = notesDatabaseDao.getTags().first()
        assertThat(result).isEqualTo(TestData.tags)
    }

    @Test
    fun testInsertNote() = runTest {
        val result = notesDatabaseDao.insertNote(TestData.note)
        assertThat(result).isEqualTo(1)
    }

    @Test
    fun testUpdateNote() = runTest {
        notesDatabaseDao.insertNote(TestData.note)
        val noteData = notesDatabaseDao.loadAllNotes().first().first()
        val result = notesDatabaseDao.updateNote(noteData.note.copy(noteTitle = "Updated Title"))
        assertThat(result).isEqualTo(1)
    }

    @Test
    fun testLoadAllNotes() = runTest {
        notesDatabaseDao.insertTags(TestData.tags)
        notesDatabaseDao.insertNoteWithDetails(
            note = TestData.note,
            checklistItems = TestData.checklistItems,
            tags = TestData.tags
        )
        val result = notesDatabaseDao.loadAllNotes().first()
        assertThat(result).isNotEmpty()
    }

    @Test
    fun testGetNoteById() = runTest {
        notesDatabaseDao.insertNote(TestData.note)
        val noteData = notesDatabaseDao.loadAllNotes().first().first()
        assertThat(noteData.note).isEqualTo(TestData.note)
    }
}

object TestData {
    val note =
        Note(
            noteId = 1,
            noteTitle = "Test Note",
            noteText = "This is a test note",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            isPinned = false,
            isArchived = false,
            isDeleted = false,
            color = NoteColor.Blue,
            isCheckList = true,
            reminderTime = System.currentTimeMillis(),
            attachments = listOf("attachment1", "attachment2")
        )

    val checklistItems =
        listOf(
            CheckListItem(checkListItemId = 1, noteId = 0, isChecked = false),
            CheckListItem(checkListItemId = 2, noteId = 0, isChecked = true)
        )

    val tags =
        listOf(
            Tag(tagId = 1, name = "tag1", color = TagColor.Blue),
            Tag(tagId = 2, name = "tag2", color = TagColor.Red)
        )

    val tag = Tag(tagId = 1, name = "tag1", color = TagColor.Blue)
}
