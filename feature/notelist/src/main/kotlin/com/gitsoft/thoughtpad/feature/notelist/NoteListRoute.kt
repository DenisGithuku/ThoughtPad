
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

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridItemScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.LottieCompositionFactory
import com.gitsoft.thoughtpad.core.model.Note
import com.gitsoft.thoughtpad.feature.notelist.components.DrawerContent
import com.gitsoft.thoughtpad.feature.notelist.components.DrawerItem
import com.gitsoft.thoughtpad.feature.notelist.components.LoadingIndicator
import com.gitsoft.thoughtpad.feature.notelist.components.NoNotesInCategoryIndicator
import com.gitsoft.thoughtpad.feature.notelist.components.NoNotesIndicator
import com.gitsoft.thoughtpad.feature.notelist.components.NoNotesOnSearchIndicator
import com.gitsoft.thoughtpad.feature.notelist.components.NoteActionBottomSheet
import com.gitsoft.thoughtpad.feature.notelist.components.NoteItemCard
import com.gitsoft.thoughtpad.feature.notelist.components.SectionSeparator
import com.gitsoft.thoughtpad.feature.notelist.components.SidebarRoute
import core.gitsoft.thoughtpad.core.toga.components.button.TogaFloatingActionButton
import core.gitsoft.thoughtpad.core.toga.components.button.TogaIconButton
import core.gitsoft.thoughtpad.core.toga.components.input.TogaSearchBar
import core.gitsoft.thoughtpad.core.toga.components.scaffold.TogaBasicScaffold
import core.gitsoft.thoughtpad.core.toga.components.text.TogaMediumBody
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun NoteListRoute(
    viewModel: NoteListViewModel = koinViewModel(),
    onCreateNewNote: (Long?) -> Unit,
    onOpenSettings: () -> Unit,
    onOpenTags: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    NoteListScreen(
        state = state,
        onCreateNewNote = onCreateNewNote,
        onOpenSettings = onOpenSettings,
        onOpenTags = onOpenTags,
        onToggleNotePin = viewModel::onToggleNotePin,
        onToggleFilterDialog = viewModel::onOpenFilterDialog,
        onToggleSelectedNote = viewModel::onToggleSelectNote,
        onToggleDeleteNote = viewModel::onToggleDelete,
        onToggleArchiveNote = viewModel::onToggleArchive
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun NoteListScreen(
    state: NoteListUiState,
    onToggleSelectedNote: (Note?) -> Unit,
    onCreateNewNote: (Long?) -> Unit,
    onToggleFilterDialog: (Boolean) -> Unit,
    onOpenSettings: () -> Unit,
    onOpenTags: () -> Unit,
    onToggleNotePin: (Long, Boolean) -> Unit,
    onToggleArchiveNote: (ArchiveState) -> Unit,
    onToggleDeleteNote: (DeleteState) -> Unit
) {
    var query: String by rememberSaveable { mutableStateOf("") }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    var selectedDrawerItem: DrawerItem by remember { mutableStateOf(DrawerItem.All) }

    val scope = rememberCoroutineScope()

    val allSercheableNotes by
        remember(state.notes, query) {
            derivedStateOf {
                if (query.isNotEmpty()) {
                    state.notes.filter { noteData ->
                        noteData.note.noteTitle?.contains(query, true) == true ||
                            noteData.note.noteText?.contains(query, true) == true ||
                            noteData.checkListItems.any { it.text?.contains(query, true) == true } ||
                            noteData.tags.any { it.name?.contains(query, true) == true }
                    }
                } else {
                    state.notes
                }
            }
        }

    val trash = allSercheableNotes.filter { it.note.isDeleted }

    val reminders = allSercheableNotes.filter { it.note.reminderTime != null }

    val archivedNotes = allSercheableNotes.filter { it.note.isArchived }

    val allFilteredNotes = allSercheableNotes.filterNot { it.note.isDeleted || it.note.isArchived }

    val pinnedNotes by
        remember(allFilteredNotes, query) {
            derivedStateOf { allFilteredNotes.filter { it.note.isPinned } }
        }

    val allOtherNotes by
        remember(allFilteredNotes, query) {
            derivedStateOf { allFilteredNotes.filter { !it.note.isPinned } }
        }

    val snackbarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current

    DisposableEffect(Unit) {
        LottieCompositionFactory.fromAsset(context, "no_notes.json")
        onDispose { LottieCompositionFactory.clearCache(context) }
    }

    LaunchedEffect(state.archiveState, snackbarHostState) {
        if (state.archiveState.isArchived && state.archiveState.noteId != null) {
            val result =
                snackbarHostState.showSnackbar(
                    message = context.getString(R.string.note_archived),
                    duration = SnackbarDuration.Short,
                    actionLabel = context.getString(R.string.undo)
                )
            when (result) {
                SnackbarResult.Dismissed -> {
                    onToggleArchiveNote(ArchiveState(noteId = null))
                }
                SnackbarResult.ActionPerformed -> {
                    onToggleArchiveNote(ArchiveState(isArchived = false, noteId = state.archiveState.noteId))
                }
            }
        }
    }

    LaunchedEffect(state.deleteState, snackbarHostState) {
        if (state.deleteState.isDeleted && state.deleteState.noteId != null) {
            val result =
                snackbarHostState.showSnackbar(
                    message = context.getString(R.string.note_deleted),
                    duration = SnackbarDuration.Short,
                    actionLabel = context.getString(R.string.undo)
                )

            when (result) {
                SnackbarResult.Dismissed -> {
                    onToggleDeleteNote(DeleteState(noteId = null))
                }
                SnackbarResult.ActionPerformed -> {
                    onToggleDeleteNote(DeleteState(isDeleted = false, noteId = state.deleteState.noteId))
                }
            }
        }
    }

    BackHandler(drawerState.isOpen || query.isNotEmpty() || selectedDrawerItem != DrawerItem.All) {
        if (drawerState.isOpen) {
            scope.launch { drawerState.close() }
        }

        if (query.isNotEmpty()) {
            query = ""
        }

        if (selectedDrawerItem != DrawerItem.All) {
            selectedDrawerItem = DrawerItem.All
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest) {
                DrawerContent(
                    selectedDrawerItem = selectedDrawerItem,
                    onOpenDrawerItem = {
                        scope.launch { drawerState.close() }
                        selectedDrawerItem = it
                    },
                    onNavigateToRoute = { route ->
                        scope.launch { drawerState.close() }
                        when (route) {
                            SidebarRoute.Settings -> onOpenSettings()
                            SidebarRoute.Tags -> onOpenTags()
                        }
                    }
                )
            }
        }
    ) {
        TogaBasicScaffold(snackbarHostState = snackbarHostState) { innerPadding ->
            Box(
                modifier =
                    Modifier.fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .imeNestedScroll()
            ) {
                val noteListState = rememberLazyStaggeredGridState()

                val isAddNoteButtonVisible by
                    remember(noteListState) {
                        derivedStateOf {
                            // Always show button if within the pinned section (first few items)
                            (noteListState.firstVisibleItemIndex <= 2 &&
                                noteListState.firstVisibleItemScrollOffset <= 100) ||
                                // Show button if at the beginning of the "Other" section (likely starts after
                                // pinned
                                // section)
                                (noteListState.layoutInfo.totalItemsCount < 100 &&
                                    noteListState.firstVisibleItemIndex == pinnedNotes.size + 1 &&
                                    noteListState.firstVisibleItemScrollOffset == 0)
                        }
                    }

                Column(
                    modifier =
                        Modifier.fillMaxSize().imeNestedScroll().padding(PaddingValues(horizontal = 16.dp))
                ) {
                    TopRow(
                        query = query,
                        onToggleSideBar = {
                            scope.launch {
                                if (drawerState.isOpen) {
                                    drawerState.close()
                                } else {
                                    drawerState.open()
                                }
                            }
                        },
                        onQueryChange = { query = it }
                    )

                    if (state.isLoading) {
                        LoadingIndicator()
                        return@Column
                    }

                    if (pinnedNotes.isEmpty() && allOtherNotes.isEmpty() && query.isNotEmpty()) {
                        NoNotesOnSearchIndicator(modifier = Modifier)
                        return@Column
                    }

                    if (state.isFilterDialogVisible) {
                        state.selectedNote?.let {
                            NoteActionBottomSheet(
                                noteId = state.selectedNote.noteId,
                                isPinned = state.selectedNote.isPinned,
                                isArchived = state.selectedNote.isArchived,
                                isDeleted = state.selectedNote.isDeleted,
                                onEdit = {
                                    onCreateNewNote(it)
                                    onToggleSelectedNote(null)
                                    onToggleFilterDialog(false)
                                },
                                onTogglePin = onToggleNotePin,
                                onShare = {},
                                onToggleArchive = { id, isArchived ->
                                    onToggleArchiveNote(ArchiveState(isArchived = isArchived, noteId = id))
                                    onToggleSelectedNote(null)
                                    onToggleFilterDialog(false)
                                },
                                onToggleDelete = { id, isDeleted ->
                                    onToggleDeleteNote(DeleteState(isDeleted = isDeleted, noteId = id))
                                    onToggleSelectedNote(null)
                                    onToggleFilterDialog(false)
                                },
                                onDismiss = {
                                    onToggleSelectedNote(null)
                                    onToggleFilterDialog(false)
                                }
                            )
                        }
                    }

                    AnimatedContent(
                        targetState = pinnedNotes.isEmpty() && allOtherNotes.isEmpty(),
                        label = "Note List Visibility State"
                    ) { isEmpty ->
                        if (isEmpty) {
                            NoNotesIndicator(modifier = Modifier)
                        } else {
                            LazyVerticalStaggeredGrid(
                                state = noteListState,
                                modifier = Modifier.fillMaxSize().imeNestedScroll(),
                                columns = StaggeredGridCells.Fixed(2),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalItemSpacing = 8.dp
                            ) {
                                when (selectedDrawerItem) {
                                    DrawerItem.All -> {
                                        item(span = StaggeredGridItemSpan.FullLine) {
                                            AnimatedVisibility(visible = pinnedNotes.isNotEmpty()) {
                                                SectionSeparator(title = R.string.pinned_notes)
                                            }
                                        }
                                        items(items = pinnedNotes, key = { it.note.noteId }) { noteData ->
                                            NoteItemCard(
                                                modifier =
                                                    Modifier.animateItem(
                                                        fadeInSpec = tween(durationMillis = 300, delayMillis = 100),
                                                        fadeOutSpec = tween(durationMillis = 300, delayMillis = 100),
                                                        placementSpec =
                                                            spring( // Controls the placement animation
                                                                dampingRatio = Spring.DampingRatioLowBouncy,
                                                                stiffness = Spring.StiffnessLow
                                                            )
                                                    ),
                                                isDarkTheme = state.isDarkTheme,
                                                isSelected = state.selectedNote?.noteId == noteData.note.noteId,
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
                                                SectionSeparator(
                                                    modifier = Modifier.padding(4.dp),
                                                    title = R.string.other_notes
                                                )
                                            }
                                        }

                                        items(items = allOtherNotes, key = { it.note.noteId }) { noteData ->
                                            NoteItemCard(
                                                modifier = Modifier.then(animateNoteItemCard()),
                                                isDarkTheme = state.isDarkTheme,
                                                isSelected = state.selectedNote?.noteId == noteData.note.noteId,
                                                noteData = noteData,
                                                onClick = { onCreateNewNote(noteData.note.noteId) },
                                                onLongClick = {
                                                    onToggleSelectedNote(noteData.note)
                                                    onToggleFilterDialog(true)
                                                }
                                            )
                                        }
                                    }
                                    DrawerItem.Archived -> {
                                        if (archivedNotes.isEmpty()) {
                                            item(span = StaggeredGridItemSpan.FullLine) {
                                                Box(
                                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    NoNotesInCategoryIndicator(category = R.string.no_archived)
                                                }
                                            }
                                        } else {
                                            items(items = archivedNotes, key = { it.note.noteId }) { noteData ->
                                                NoteItemCard(
                                                    modifier = Modifier.then(animateNoteItemCard()),
                                                    isDarkTheme = state.isDarkTheme,
                                                    isSelected = state.selectedNote?.noteId == noteData.note.noteId,
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
                                    DrawerItem.Reminders -> {
                                        if (reminders.isEmpty()) {
                                            item(span = StaggeredGridItemSpan.FullLine) {
                                                Box(
                                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    NoNotesInCategoryIndicator(
                                                        modifier = Modifier.fillMaxSize(),
                                                        category = R.string.no_reminders
                                                    )
                                                }
                                            }
                                        } else {
                                            items(items = reminders, key = { it.note.noteId }) { noteData ->
                                                NoteItemCard(
                                                    modifier = Modifier.then(animateNoteItemCard()),
                                                    isDarkTheme = state.isDarkTheme,
                                                    isSelected = state.selectedNote?.noteId == noteData.note.noteId,
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
                                    DrawerItem.Trash -> {
                                        item(span = StaggeredGridItemSpan.FullLine) {
                                            TogaMediumBody(
                                                maxLines = 10,
                                                text = stringResource(id = R.string.trash_info),
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                        if (trash.isEmpty()) {
                                            item(span = StaggeredGridItemSpan.FullLine) {
                                                Box(
                                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    NoNotesInCategoryIndicator(
                                                        modifier = Modifier.fillMaxSize(),
                                                        category = R.string.no_trash
                                                    )
                                                }
                                            }
                                        } else {
                                            items(items = trash, key = { it.note.noteId }) { noteData ->
                                                NoteItemCard(
                                                    modifier = Modifier.then(animateNoteItemCard()),
                                                    isDarkTheme = state.isDarkTheme,
                                                    isSelected = state.selectedNote?.noteId == noteData.note.noteId,
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
                                }
                            }
                        }
                    }
                }

                AnimatedVisibility(
                    visible = isAddNoteButtonVisible,
                    modifier =
                        Modifier.align(Alignment.BottomEnd).padding(bottom = 16.dp, end = 16.dp).imePadding(),
                    enter =
                        scaleIn(
                            initialScale = 0.6f, // Start smaller for a zoom-in effect
                            animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                        ) + fadeIn(animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)),
                    exit =
                        scaleOut(
                            targetScale = 0.6f, // End smaller for a zoom-out effect
                            animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
                        ) + fadeOut(animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing))
                ) {
                    TogaFloatingActionButton(
                        icon = R.drawable.ic_add,
                        contentDescription = R.string.add_note,
                        onClick = { onCreateNewNote(null) }
                    )
                }
            }
        }
    }
}

fun LazyStaggeredGridItemScope.animateNoteItemCard(): Modifier {
    return Modifier.animateItem(
        fadeInSpec = tween(durationMillis = 300, delayMillis = 100),
        fadeOutSpec = tween(durationMillis = 300, delayMillis = 100),
        placementSpec =
            spring( // Controls the placement animation
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessLow
            )
    )
}

@Composable
fun TopRow(query: String, onToggleSideBar: () -> Unit, onQueryChange: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(PaddingValues(vertical = 16.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TogaIconButton(
            modifier = Modifier.sizeIn(24.dp),
            icon = R.drawable.ic_side_menu,
            contentDescription = R.string.sidebar_toggle,
            onClick = onToggleSideBar
        )
        TogaSearchBar(
            modifier = Modifier.weight(1f),
            query = query,
            onQueryChange = onQueryChange,
            onSearch = {}
        )
    }
}
