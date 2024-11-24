
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
package core.gitsoft.thoughtpad.core.data.repository.fakes

import com.gitsoft.thoughtpad.core.model.CheckListItem
import com.gitsoft.thoughtpad.core.model.DataWithNotesCheckListItemsAndTags
import com.gitsoft.thoughtpad.core.model.Note
import com.gitsoft.thoughtpad.core.model.NoteColor
import com.gitsoft.thoughtpad.core.model.Tag
import com.gitsoft.thoughtpad.core.model.TagColor
import core.gitsoft.thoughtpad.core.data.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.InputStream

class FakeNotesRepository : NotesRepository {

    private var _tagsData =
        MutableStateFlow(
            listOf(
                Tag(tagId = 1, name = "Hobby", color = TagColor.Brown),
                Tag(tagId = 2, name = "Urgent", color = TagColor.Red),
                Tag(tagId = 3, name = "Work", color = TagColor.Blue)
            )
        )

    private var _notesData =
        MutableStateFlow(
            (0..10).map { index ->
                DataWithNotesCheckListItemsAndTags(
                    note =
                        Note(
                            noteId = index.toLong(),
                            noteTitle = "Title $index",
                            noteText = "Text $index",
                            createdAt = System.currentTimeMillis(),
                            updatedAt = System.currentTimeMillis(),
                            isPinned = false,
                            isArchived = false,
                            color = NoteColor.Blue,
                            isDeleted = false,
                            isCheckList = true,
                            reminderTime = System.currentTimeMillis(),
                            attachments = listOf()
                        ),
                    checkListItems =
                        (0..4).map {
                            CheckListItem(
                                checkListItemId = it.toLong(),
                                noteId = index.toLong(),
                                text = "Checklist $it",
                                isChecked = true
                            )
                        },
                    tags = _tagsData.value
                )
            }
        )

    override val allNotes: Flow<List<DataWithNotesCheckListItemsAndTags>>
        get() = _notesData

    override suspend fun getNoteWithDataById(id: Long): DataWithNotesCheckListItemsAndTags {
        return _notesData.value.first { it.note.noteId == id }
    }

    override suspend fun getNoteById(id: Long): Note {
        return _notesData.value.first { it.note.noteId == id }.note
    }

    override suspend fun updateNoteWithDetails(
        note: Note,
        checklistItems: List<CheckListItem>,
        tags: List<Tag>
    ) {
        _notesData.value =
            _notesData.value.map {
                if (it.note.noteId == note.noteId) {
                    DataWithNotesCheckListItemsAndTags(note, checklistItems, tags)
                } else {
                    it
                }
            }
    }

    override suspend fun updateNote(note: Note): Int {
        val noteData =
            (_notesData.value.find { it.note.noteId == note.noteId } ?: return 0).copy(note = note)
        _notesData.value =
            _notesData.value.map {
                if (it.note.noteId == note.noteId) {
                    noteData
                } else {
                    it
                }
            }
        return 1
    }

    override suspend fun insertNoteWithDetails(
        note: Note,
        checklistItems: List<CheckListItem>,
        tags: List<Tag>
    ): Long {
        val allNotesData = _notesData.value.toMutableList()
        val isAdded = allNotesData.add(DataWithNotesCheckListItemsAndTags(note, checklistItems, tags))
        _notesData.value = allNotesData
        return if (isAdded) 1 else 0
    }

    override suspend fun deleteNoteById(id: Long): Int {
        val allNotes = _notesData.value.toMutableList()
        val isRemoved = allNotes.removeIf { it.note.noteId == id }
        _notesData.value = allNotes
        return if (isRemoved) 1 else 0
    }

    override suspend fun encryptPassword(password: String): ByteArray? = null

    override suspend fun decryptPassword(inputStream: InputStream): ByteArray? = null

    override suspend fun insertTags(tags: List<Tag>) {
        val allTags = _tagsData.value.toMutableList()
        allTags.addAll(tags)
        _tagsData.value = allTags
    }

    override suspend fun insertTag(tag: Tag): Long {
        val allTags = _tagsData.value.toMutableList()
        val isAdded = allTags.add(tag)
        _tagsData.value = allTags
        return if (isAdded) 1L else 0L
    }

    override suspend fun updateTag(tag: Tag): Int {
        val isPresent = _tagsData.value.any { it.tagId == tag.tagId }
        val allTags = _tagsData.value.toMutableList()
        allTags[tag.tagId.toInt()] = tag
        _tagsData.value = allTags
        return if (isPresent) 1 else 0
    }

    override suspend fun deleteTag(tag: Tag): Int {
        val allTags = _tagsData.value.toMutableList()
        val removed = allTags.removeIf { it.tagId == tag.tagId }
        _tagsData.value = allTags
        return if (removed) 1 else 0
    }

    override suspend fun getTagById(tagId: Long): Tag {
        return _tagsData.value[tagId.toInt()]
    }

    override val allTags: Flow<List<Tag>>
        get() = _tagsData
}
