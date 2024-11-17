
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

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gitsoft.thoughtpad.core.model.DataWithNotesCheckListItemsAndTags
import com.gitsoft.thoughtpad.core.model.Note
import com.gitsoft.thoughtpad.core.toga.components.text.TogaMediumBody
import com.gitsoft.thoughtpad.feature.notelist.R
import com.gitsoft.thoughtpad.feature.notelist.TestTags
import com.gitsoft.thoughtpad.feature.notelist.components.NoNotesInCategoryIndicator
import com.gitsoft.thoughtpad.feature.notelist.components.NoteItemCard
import com.gitsoft.thoughtpad.feature.notelist.components.animateNoteItemCard

@OptIn(ExperimentalSharedTransitionApi::class)
fun LazyStaggeredGridScope.trash(
    trash: List<DataWithNotesCheckListItemsAndTags>,
    isDarkTheme: Boolean,
    selectedNote: Note? = null,
    onCreateNewNote: (noteId: Long?) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onToggleFilterDialog: (show: Boolean) -> Unit,
    onToggleSelectedNote: (note: Note?) -> Unit
) {
    item(span = StaggeredGridItemSpan.FullLine) {
        TogaMediumBody(
            maxLines = 10,
            text = stringResource(id = R.string.trash_info),
            textAlign = TextAlign.Center
        )
    }
    if (trash.isEmpty()) {
        item(span = StaggeredGridItemSpan.FullLine) {
            Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                NoNotesInCategoryIndicator(modifier = Modifier.fillMaxSize(), category = R.string.no_trash)
            }
        }
    } else {
        items(items = trash, key = { it.note.noteId }) { noteData ->
            NoteItemCard(
                modifier = Modifier.then(animateNoteItemCard()).testTag(TestTags.NOTE_ITEM_CARD),
                isDarkTheme = isDarkTheme,
                isSelected = selectedNote?.noteId == noteData.note.noteId,
                noteData = noteData,
                onClick = { onCreateNewNote(noteData.note.noteId) },
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope,
                onLongClick = {
                    onToggleSelectedNote(noteData.note)
                    onToggleFilterDialog(true)
                }
            )
        }
    }
}
