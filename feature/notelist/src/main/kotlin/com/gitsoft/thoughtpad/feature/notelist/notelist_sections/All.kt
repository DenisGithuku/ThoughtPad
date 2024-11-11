
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
package com.gitsoft.thoughtpad.feature.notelist.notelist_sections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.gitsoft.thoughtpad.core.model.DataWithNotesCheckListItemsAndTags
import com.gitsoft.thoughtpad.core.model.Note
import com.gitsoft.thoughtpad.feature.notelist.R
import com.gitsoft.thoughtpad.feature.notelist.TestTags
import com.gitsoft.thoughtpad.feature.notelist.components.NoteItemCard
import com.gitsoft.thoughtpad.feature.notelist.components.NotesAndTasksCompleted
import com.gitsoft.thoughtpad.feature.notelist.components.SectionSeparator
import com.gitsoft.thoughtpad.feature.notelist.components.animateNoteItemCard

fun LazyStaggeredGridScope.all(
    pinnedNotes: List<DataWithNotesCheckListItemsAndTags>,
    allOtherNotes: List<DataWithNotesCheckListItemsAndTags>,
    selectedNote: Note? = null,
    isDarkTheme: Boolean,
    onCreateNewNote: (noteId: Long?) -> Unit,
    onToggleFilterDialog: (show: Boolean) -> Unit,
    onToggleSelectedNote: (note: Note?) -> Unit
) {
    if (pinnedNotes.isEmpty() && allOtherNotes.isEmpty()) {
        item(span = StaggeredGridItemSpan.FullLine) {
            NotesAndTasksCompleted(modifier = Modifier.testTag(TestTags.ALL_NOTES_AND_TASKS_COMPLETED))
        }
    } else {
        item(span = StaggeredGridItemSpan.FullLine) {
            AnimatedVisibility(visible = pinnedNotes.isNotEmpty()) {
                SectionSeparator(title = R.string.pinned_notes)
            }
        }
        items(items = pinnedNotes, key = { it.note.noteId }) { noteData ->
            NoteItemCard(
                modifier = Modifier.then(animateNoteItemCard()).testTag(TestTags.NOTE_ITEM_CARD),
                isDarkTheme = isDarkTheme,
                isSelected = selectedNote?.noteId == noteData.note.noteId,
                noteData = noteData,
                onClick = { onCreateNewNote(noteData.note.noteId) },
                onLongClick = {
                    onToggleSelectedNote(noteData.note)
                    onToggleFilterDialog(true)
                }
            )
        }

        item(span = StaggeredGridItemSpan.FullLine) {
            AnimatedVisibility(visible = allOtherNotes.isNotEmpty()) {
                SectionSeparator(modifier = Modifier.padding(4.dp), title = R.string.other_notes)
            }
        }

        items(items = allOtherNotes, key = { it.note.noteId }) { noteData ->
            NoteItemCard(
                modifier = Modifier.then(animateNoteItemCard()).testTag(TestTags.NOTE_ITEM_CARD),
                isDarkTheme = isDarkTheme,
                isSelected = selectedNote?.noteId == noteData.note.noteId,
                noteData = noteData,
                onClick = { onCreateNewNote(noteData.note.noteId) },
                onLongClick = {
                    onToggleSelectedNote(noteData.note)
                    onToggleFilterDialog(true)
                }
            )
        }
    }
}
