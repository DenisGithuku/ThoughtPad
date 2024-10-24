
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

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gitsoft.thoughtpad.feature.notelist.components.LoadingIndicator
import com.gitsoft.thoughtpad.feature.notelist.components.NoNotesIndicator
import core.gitsoft.thoughtpad.core.toga.components.button.TogaIconButton
import core.gitsoft.thoughtpad.core.toga.components.input.TogaSearchBar
import core.gitsoft.thoughtpad.core.toga.components.text.TogaSmallBody
import core.gitsoft.thoughtpad.core.toga.components.text.TogaSmallTitle
import core.gitsoft.thoughtpad.core.toga.theme.toComposeColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun NoteListRoute(
    viewModel: NoteListViewModel = koinViewModel(),
    onCreateNewNote: () -> Unit,
    onOpenNoteDetail: (Long) -> Unit,
    onOpenSettings: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    NoteListScreen(state = state, onCreateNewNote, onOpenNoteDetail, onOpenSettings)
}

@Composable
internal fun NoteListScreen(
    state: NoteListUiState,
    onCreateNewNote: () -> Unit,
    onOpenNoteDetail: (Long) -> Unit,
    onOpenSettings: () -> Unit
) {
    var query: String by rememberSaveable { mutableStateOf("") }

    Column(
        modifier =
            Modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(PaddingValues(horizontal = 16.dp))
    ) {
        TopRow(
            query = query,
            onOpenSettings = onOpenSettings,
            onCreateNewNote = onCreateNewNote,
            onQueryChange = { query = it }
        )

        if (state.isLoading) LoadingIndicator()

        AnimatedContent(targetState = state.notes.isEmpty(), label = "Note List Visibility State") {
            isEmpty ->
            if (isEmpty) {
                NoNotesIndicator(modifier = Modifier)
            } else {
                LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(2)) {
                    items(state.notes.size, key = { state.notes[it].note.noteId }) { index ->
                        val noteData = state.notes[index]
                        NoteItem(
                            note =
                                NoteListItem(
                                    id = noteData.note.noteId,
                                    title = noteData.note.noteTitle ?: "",
                                    content = noteData.note.noteText ?: "",
                                    color = noteData.note.color.toComposeColor()
                                ),
                            onClick = { onOpenNoteDetail(noteData.note.noteId) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NoteItem(note: NoteListItem, onClick: () -> Unit) {
    Box(
        modifier =
            Modifier.fillMaxWidth()
                .padding(4.dp)
                .clip(shape = MaterialTheme.shapes.medium)
                .border(
                    width = 0.8.dp,
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.04f)
                )
                .background(color = note.color, shape = MaterialTheme.shapes.medium)
                .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            TogaSmallTitle(text = note.title, maxLines = 1)
            Spacer(modifier = Modifier.height(8.dp))
            TogaSmallBody(text = note.content, maxLines = 3)
        }
    }
}

@Composable
fun TopRow(
    query: String,
    onOpenSettings: () -> Unit,
    onCreateNewNote: () -> Unit,
    onQueryChange: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(PaddingValues(16.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TogaSearchBar(
            modifier = Modifier.weight(1f),
            query = query,
            onQueryChange = onQueryChange,
            onSearch = {}
        )
        TogaIconButton(
            modifier = Modifier.sizeIn(24.dp),
            icon = R.drawable.ic_add_circle,
            contentDescription = R.string.add_note,
            onClick = onCreateNewNote
        )
        TogaIconButton(
            modifier = Modifier.sizeIn(24.dp),
            icon = R.drawable.ic_settings,
            contentDescription = R.string.settings,
            onClick = onOpenSettings
        )
    }
}

data class NoteListItem(val id: Long, val title: String, val content: String, val color: Color)
