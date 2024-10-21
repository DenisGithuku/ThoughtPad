
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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import com.gitsoft.thoughtpad.feature.notelist.components.NoNotesIndicator
import core.gitsoft.thoughtpad.core.toga.components.TogaIconButton
import core.gitsoft.thoughtpad.core.toga.components.TogaStandardScaffold
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NoteListScreen(
    state: NoteListUiState,
    onCreateNewNote: () -> Unit,
    onOpenNoteDetail: (Long) -> Unit,
    onOpenSettings: () -> Unit
) {
    TogaStandardScaffold(
        title = R.string.notes_list_title,
        actions = {
            TogaIconButton(
                icon = R.drawable.ic_settings,
                contentDescription = R.string.settings,
                onClick = onOpenSettings
            )
        },
        fab = {
            TogaIconButton(
                icon = R.drawable.ic_add,
                contentDescription = R.string.add_note,
                onClick = onCreateNewNote
            )
        }
    ) { innerPadding ->
        val textFieldState = rememberTextFieldState()
        var expanded by rememberSaveable { mutableStateOf(false) }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                SearchBar(
                    modifier = Modifier.semantics { traversalIndex = 0f },
                    inputField = {
                        SearchBarDefaults.InputField(
                            onSearch = { expanded = false },
                            expanded = expanded,
                            onExpandedChange = { expanded = it },
                            placeholder = { Text("Hinted search text") },
                            leadingIcon = {
                                Icon(painter = painterResource(R.drawable.ic_search), contentDescription = null)
                            },
                            onQueryChange = { query -> textFieldState.edit { append(query) } },
                            query = textFieldState.text.toString()
                        )
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {}
            }

            if (state.notes.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) { NoNotesIndicator() }
            } else {
                items(state.notes.size, key = { state.notes[it].note.noteId }) { index ->
                    val noteData = state.notes[index]
                    NoteItem(
                        note =
                            NoteListItem(
                                id = noteData.note.noteId,
                                title = noteData.note.noteTitle ?: "",
                                content = noteData.note.noteText ?: ""
                            ),
                        onClick = { onOpenNoteDetail(noteData.note.noteId) }
                    )
                }
            }
        }
    }
}

@Composable
fun NoteItem(note: NoteListItem, onClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp).clickable(onClick = onClick)) {
            Text(text = note.title, style = MaterialTheme.typography.displaySmall)
            Text(text = note.content, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

data class NoteListItem(val id: Long, val title: String, val content: String)
