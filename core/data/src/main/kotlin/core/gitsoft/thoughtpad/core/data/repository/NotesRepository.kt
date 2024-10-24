
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

import com.gitsoft.thoughtpad.core.model.CheckListItem
import com.gitsoft.thoughtpad.core.model.DataWithNotesCheckListItemsAndTags
import com.gitsoft.thoughtpad.core.model.Note
import com.gitsoft.thoughtpad.core.model.Tag
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    val allNotes: Flow<List<DataWithNotesCheckListItemsAndTags>>

    suspend fun getNoteWithDataById(id: Long): DataWithNotesCheckListItemsAndTags

    suspend fun getNoteById(id: Long): Note

    suspend fun updateNoteWithDetails(
        note: Note,
        checklistItems: List<CheckListItem>,
        tags: List<Tag>
    )

    suspend fun updateNote(note: Note): Int

    suspend fun insertNoteWithDetails(
        note: Note,
        checklistItems: List<CheckListItem>,
        tags: List<Tag>
    ): Long

    suspend fun insertTags(tags: List<Tag>)

    suspend fun insertTag(tag: Tag): Long

    suspend fun getTagById(tagId: Long): Tag

    val allTags: Flow<List<Tag>>
}
