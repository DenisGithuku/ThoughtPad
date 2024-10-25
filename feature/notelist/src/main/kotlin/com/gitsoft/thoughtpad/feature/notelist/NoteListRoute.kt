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
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.DisposableEffectScope
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.model.LottieCompositionCache
import com.gitsoft.thoughtpad.core.model.Note
import com.gitsoft.thoughtpad.feature.notelist.components.LoadingIndicator
import com.gitsoft.thoughtpad.feature.notelist.components.NoNotesIndicator
import com.gitsoft.thoughtpad.feature.notelist.components.NoNotesOnSearchIndicator
import com.gitsoft.thoughtpad.feature.notelist.components.NoteActionBottomSheet
import com.gitsoft.thoughtpad.feature.notelist.components.NoteItemCard
import com.gitsoft.thoughtpad.feature.notelist.components.SectionSeparator
import core.gitsoft.thoughtpad.core.toga.components.button.TogaFloatingActionButton
import core.gitsoft.thoughtpad.core.toga.components.button.TogaIconButton
import core.gitsoft.thoughtpad.core.toga.components.input.TogaSearchBar
import core.gitsoft.thoughtpad.core.toga.components.scaffold.TogaBasicScaffold
import org.koin.androidx.compose.koinViewModel

@Composable
fun NoteListRoute(
    viewModel: NoteListViewModel = koinViewModel(),
    onCreateNewNote: () -> Unit,
    onOpenNoteDetail: (Long) -> Unit,
    onOpenSettings: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    NoteListScreen(
        state = state,
        onCreateNewNote = onCreateNewNote,
        onOpenNoteDetail = onOpenNoteDetail,
        onOpenSettings = onOpenSettings,
        onToggleNotePin = viewModel::onToggleNotePin,
        onToggleNoteFavourite = viewModel::onToggleNoteFavourite,
        onToggleFilterDialog = viewModel::onOpenFilterDialog,
        onToggleSelectedNote = viewModel::onToggleSelectNote,
        onEditNote = onOpenNoteDetail,
        onToggleDeleteNote = viewModel::onToggleDelete,
        onToggleArchiveNote = viewModel::onToggleArchive
    )
}

@Composable
internal fun NoteListScreen(
    state: NoteListUiState,
    onToggleSelectedNote: (Note?) -> Unit,
    onCreateNewNote: () -> Unit,
    onToggleFilterDialog: (Boolean) -> Unit,
    onOpenNoteDetail: (Long) -> Unit,
    onOpenSettings: () -> Unit,
    onToggleNotePin: (Long, Boolean) -> Unit,
    onToggleNoteFavourite: (Long, Boolean) -> Unit,
    onEditNote: (Long) -> Unit,
    onToggleArchiveNote: (ArchiveState) -> Unit,
    onToggleDeleteNote: (DeleteState) -> Unit,
) {
    var query: String by rememberSaveable { mutableStateOf("") }

    val filteredNotes by remember(state.notes, query) {
        derivedStateOf {
            if (query.isNotEmpty()) {
                state.notes.filter { noteData ->
                    noteData.note.noteTitle?.contains(query, true) == true ||
                            noteData.note.noteText?.contains(query, true) == true || noteData.checkListItems.any { it.text?.contains(query, true) == true } || noteData.tags.any { it.name?.contains(query, true) == true }
                }
            } else {
                state.notes
            }
        }
    }

    val pinnedNotes by remember(
        filteredNotes, query
    ) { derivedStateOf { filteredNotes.filter { it.note.isPinned } } }

    val otherNotes by remember(
        filteredNotes, query
    ) { derivedStateOf { filteredNotes.filter { !it.note.isPinned } } }

    val snackbarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current

   DisposableEffect(Unit) {
       LottieCompositionFactory.fromAsset(context, "no_notes.json")
       onDispose {
           LottieCompositionFactory.clearCache(context)
       }
   }

    TogaBasicScaffold(
        snackbarHostState = snackbarHostState
    ) { innerPadding ->

        LaunchedEffect(
            state.archiveState, snackbarHostState
        ) {
            if (state.archiveState.isArchived && state.archiveState.noteId != null) {

                val result = snackbarHostState.showSnackbar(
                    message = context.getString(R.string.note_archived),
                    duration = SnackbarDuration.Short,
                    actionLabel = context.getString(R.string.undo)
                )
                when (result) {
                    SnackbarResult.Dismissed -> Unit
                    SnackbarResult.ActionPerformed -> {
                        onToggleArchiveNote(
                            ArchiveState(
                                isArchived = false, noteId = state.archiveState.noteId
                            )
                        )
                    }
                }
            }

        }

        LaunchedEffect(state.deleteState, snackbarHostState) {
            if (state.deleteState.isDeleted && state.deleteState.noteId != null) {

                val result = snackbarHostState.showSnackbar(
                    message = context.getString(R.string.note_deleted),
                    duration = SnackbarDuration.Short,
                    actionLabel = context.getString(R.string.undo)
                )

                when (result) {
                    SnackbarResult.Dismissed -> Unit
                    SnackbarResult.ActionPerformed -> {
                        onToggleDeleteNote(
                            DeleteState(
                                isDeleted = false, noteId = state.deleteState.noteId
                            )
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .consumeWindowInsets(
                    WindowInsets.ime
                )
        ) {
            val noteListState = rememberLazyStaggeredGridState()

            val isAddNoteButtonVisible by remember(noteListState) {
                derivedStateOf {
                    // Always show button if within the pinned section (first few items)
                    (noteListState.firstVisibleItemIndex <= 2 && noteListState.firstVisibleItemScrollOffset <= 100) ||
                            // Show button if at the beginning of the "Other" section (likely starts after pinned section)
                            (noteListState.layoutInfo.totalItemsCount < 100 && noteListState.firstVisibleItemIndex == pinnedNotes.size + 1 && noteListState.firstVisibleItemScrollOffset == 0)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(PaddingValues(horizontal = 16.dp))
            ) {
                TopRow(
                    query = query,
                    onOpenSettings = onOpenSettings,
                    onQueryChange = { query = it })

                if (state.isLoading) {
                    LoadingIndicator()
                    return@Column
                }

                if (pinnedNotes.isEmpty() && otherNotes.isEmpty() && query.isNotEmpty()) {
                    NoNotesOnSearchIndicator(modifier = Modifier)
                    return@Column
                }

                if (state.isFilterDialogVisible) {
                    state.selectedNote?.let {
                        NoteActionBottomSheet(noteId = state.selectedNote.noteId,
                            isPinned = state.selectedNote.isPinned,
                            onEdit = {
                                onEditNote(it)
                                onToggleSelectedNote(null)
                                onToggleFilterDialog(false)
                            },
                            onTogglePin = onToggleNotePin,
                            onShare = {},
                            onArchive = {
                                onToggleArchiveNote(
                                    ArchiveState(
                                        isArchived = true, noteId = it
                                    )
                                )
                                onToggleSelectedNote(null)
                                onToggleFilterDialog(false)
                            },
                            onDelete = {
                                onToggleDeleteNote(
                                    DeleteState(
                                        isDeleted = true, noteId = it
                                    )
                                )
                                onToggleSelectedNote(null)
                                onToggleFilterDialog(false)
                            },
                            onDismiss = {
                                onToggleSelectedNote(null)
                                onToggleFilterDialog(false)
                            })
                    }
                }

                AnimatedContent(
                    targetState = pinnedNotes.isEmpty() && otherNotes.isEmpty(), label = "Note List Visibility State"
                ) { isEmpty ->
                    if (isEmpty) {
                        NoNotesIndicator(modifier = Modifier)
                    } else {
                        LazyVerticalStaggeredGrid(
                            state = noteListState,
                            columns = StaggeredGridCells.Fixed(2),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalItemSpacing = 8.dp,
                        ) {
                            item(span = StaggeredGridItemSpan.FullLine) {
                                AnimatedVisibility(visible = pinnedNotes.isNotEmpty()) {
                                    SectionSeparator(title = R.string.pinned_notes)
                                }
                            }
                            items(items = pinnedNotes, key = { it.note.noteId }) { noteData ->
                                NoteItemCard(modifier = Modifier.animateItem(
                                    fadeInSpec = tween(
                                        durationMillis = 300, delayMillis = 100
                                    ),
                                    fadeOutSpec = tween(
                                        durationMillis = 300, delayMillis = 100
                                    ),
                                    placementSpec = spring( // Controls the placement animation
                                        dampingRatio = Spring.DampingRatioLowBouncy,
                                        stiffness = Spring.StiffnessLow
                                    )
                                ),
                                    isSelected = state.selectedNote?.noteId == noteData.note.noteId,
                                    noteData = noteData,
                                    onClick = { onOpenNoteDetail(noteData.note.noteId) },
                                    onLongClick = {
                                        onToggleSelectedNote(noteData.note)
                                        onToggleFilterDialog(true)
                                    },
                                    onToggleFavourite = {
                                        onToggleNoteFavourite(
                                            noteData.note.noteId, it
                                        )
                                    })
                            }
                            item(span = StaggeredGridItemSpan.FullLine) {
                                AnimatedVisibility(visible = otherNotes.isNotEmpty()) {
                                    SectionSeparator(
                                        modifier = Modifier.padding(4.dp),
                                        title = R.string.other_notes
                                    )
                                }
                            }

                            items(items = otherNotes, key = { it.note.noteId }) { noteData ->
                                NoteItemCard(modifier = Modifier.animateItem(
                                    fadeInSpec = tween(
                                        durationMillis = 300, delayMillis = 100
                                    ),
                                    fadeOutSpec = tween(
                                        durationMillis = 300, delayMillis = 100
                                    ),
                                    placementSpec = spring( // Controls the placement animation
                                        dampingRatio = Spring.DampingRatioLowBouncy,
                                        stiffness = Spring.StiffnessLow
                                    )
                                ),
                                    isSelected = state.selectedNote?.noteId == noteData.note.noteId,
                                    noteData = noteData,
                                    onClick = { onOpenNoteDetail(noteData.note.noteId) },
                                    onLongClick = {
                                        onToggleSelectedNote(noteData.note)
                                        onToggleFilterDialog(true)
                                    },
                                    onToggleFavourite = {
                                        onToggleNoteFavourite(
                                            noteData.note.noteId, it
                                        )
                                    })
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = isAddNoteButtonVisible,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                enter = scaleIn(
                    initialScale = 0.6f, // Start smaller for a zoom-in effect
                    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                ) + fadeIn(
                    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                ),
                exit = scaleOut(
                    targetScale = 0.6f, // End smaller for a zoom-out effect
                    animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
                ) + fadeOut(
                    animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
                )
            ) {
                TogaFloatingActionButton(
                    icon = R.drawable.ic_add,
                    contentDescription = R.string.add_note,
                    onClick = onCreateNewNote
                )
            }
        }
    }
}

@Composable
fun TopRow(
    query: String, onOpenSettings: () -> Unit, onQueryChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingValues(vertical = 16.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TogaSearchBar(modifier = Modifier.weight(1f),
            query = query,
            onQueryChange = onQueryChange,
            onSearch = {})
        TogaIconButton(
            modifier = Modifier.sizeIn(24.dp),
            icon = R.drawable.ic_settings,
            contentDescription = R.string.settings,
            onClick = onOpenSettings
        )
    }
}

data class NoteListItem(val id: Long, val title: String, val content: String, val color: Color)
