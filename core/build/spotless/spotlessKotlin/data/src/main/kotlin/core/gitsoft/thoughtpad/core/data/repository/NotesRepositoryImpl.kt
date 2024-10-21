
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
package core.gitsoft.thoughtpad.core.data.repository

import com.gitsoft.thoughtpad.core.database.NotesDatabaseDao
import com.gitsoft.thoughtpad.core.model.CheckListItem
import com.gitsoft.thoughtpad.core.model.DataWithNotesCheckListItemsAndTags
import com.gitsoft.thoughtpad.core.model.Note
import com.gitsoft.thoughtpad.core.model.Tag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class NotesRepositoryImpl(private val notesDatabaseDao: NotesDatabaseDao) :
    NotesRepository {
    override val allNotes: Flow<List<DataWithNotesCheckListItemsAndTags>>
        get() = flow { emit(notesDatabaseDao.loadAllNotes()) }

    override suspend fun getNoteById(id: Int): DataWithNotesCheckListItemsAndTags {
        return notesDatabaseDao.noteById(id)
    }

    override suspend fun insert(note: Note) {
        notesDatabaseDao.insertNote(note)
    }

    override suspend fun delete(note: Note) {
        notesDatabaseDao.delete(note)
    }

    override suspend fun updateNoteWithDetails(
        note: Note,
        checklistItems: List<CheckListItem>,
        tags: List<Tag>
    ) {
        notesDatabaseDao.updateNoteWithDetails(note, checklistItems, tags)
    }

    override suspend fun insertTags(tags: List<Tag>) {
        notesDatabaseDao.insertTags(tags)
    }

    override suspend fun getTagById(tagId: Long): Tag {
        return notesDatabaseDao.getTag(tagId)
    }

    override suspend fun getAllTags(): List<Tag> {
        return notesDatabaseDao.getTags()
    }

    override suspend fun insertNoteWithDetails(
        note: Note,
        checklistItems: List<CheckListItem>,
        tags: List<Tag>
    ) {
        notesDatabaseDao.insertNoteWithDetails(note, checklistItems, tags)
    }
}
