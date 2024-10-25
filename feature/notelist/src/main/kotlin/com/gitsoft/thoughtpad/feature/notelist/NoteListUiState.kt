package com.gitsoft.thoughtpad.feature.notelist

import com.gitsoft.thoughtpad.core.model.DataWithNotesCheckListItemsAndTags
import com.gitsoft.thoughtpad.core.model.Note


data class ArchiveState(
    val isArchived: Boolean = false, val noteId: Long? = null
)

data class DeleteState(
    val isDeleted: Boolean = false, val noteId: Long? = null
)

data class NoteListUiState(
    val isLoading: Boolean = false,
    val isFilterDialogVisible: Boolean = false,
    val archiveState: ArchiveState = ArchiveState(),
    val deleteState: DeleteState = DeleteState(),
    val selectedNote: Note? = null,
    val notes: List<DataWithNotesCheckListItemsAndTags> = emptyList()
)