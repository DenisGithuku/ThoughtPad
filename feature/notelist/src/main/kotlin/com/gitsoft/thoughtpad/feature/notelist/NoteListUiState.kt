
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
package com.gitsoft.thoughtpad.feature.notelist

import com.gitsoft.thoughtpad.core.model.DataWithNotesCheckListItemsAndTags
import com.gitsoft.thoughtpad.core.model.Note
import com.gitsoft.thoughtpad.core.model.NoteListType
import com.gitsoft.thoughtpad.core.model.ReminderDisplayStyle
import com.gitsoft.thoughtpad.core.model.UserMessage

data class ArchiveState(val isArchived: Boolean = false, val noteId: Long? = null)

data class DeleteState(val isDeleted: Boolean = false, val noteId: Long? = null)

data class NoteListUiState(
    val isLoading: Boolean = false,
    val isFilterDialogVisible: Boolean = false,
    val archiveState: ArchiveState = ArchiveState(),
    val deleteState: DeleteState = DeleteState(),
    val isDarkTheme: Boolean = false,
    val selectedNote: Note? = null,
    val noteToUnlock: Note? = null,
    val selectedNoteListType: NoteListType = NoteListType.GRID,
    val reminderDisplayStyle: ReminderDisplayStyle = ReminderDisplayStyle.LIST,
    val notes: List<DataWithNotesCheckListItemsAndTags> = emptyList(),
    val unlockedNotes: List<Long> = emptyList(),
    val unlockDialogIsVisible: Boolean = false,
    val unlockNotePassword: String? = null,
    val userMessages: List<UserMessage> = emptyList()
)
