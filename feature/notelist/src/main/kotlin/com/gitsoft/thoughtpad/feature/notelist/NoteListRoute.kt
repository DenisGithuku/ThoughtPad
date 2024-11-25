
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
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.LottieCompositionFactory
import com.gitsoft.thoughtpad.core.model.Note
import com.gitsoft.thoughtpad.core.model.NoteListType
import com.gitsoft.thoughtpad.core.toga.components.button.TogaFloatingActionButton
import com.gitsoft.thoughtpad.core.toga.components.button.TogaPrimaryButton
import com.gitsoft.thoughtpad.core.toga.components.button.TogaTextButton
import com.gitsoft.thoughtpad.core.toga.components.dialog.TogaBasicDialog
import com.gitsoft.thoughtpad.core.toga.components.input.TogaTextField
import com.gitsoft.thoughtpad.core.toga.components.scaffold.TogaBasicScaffold
import com.gitsoft.thoughtpad.core.toga.components.text.TogaButtonText
import com.gitsoft.thoughtpad.core.toga.components.text.TogaSmallTitle
import com.gitsoft.thoughtpad.feature.notelist.components.DrawerContent
import com.gitsoft.thoughtpad.feature.notelist.components.DrawerItem
import com.gitsoft.thoughtpad.feature.notelist.components.LoadingIndicator
import com.gitsoft.thoughtpad.feature.notelist.components.NoNotesIndicator
import com.gitsoft.thoughtpad.feature.notelist.components.NoNotesOnSearchIndicator
import com.gitsoft.thoughtpad.feature.notelist.components.NoteActionBottomSheet
import com.gitsoft.thoughtpad.feature.notelist.components.SidebarRoute
import com.gitsoft.thoughtpad.feature.notelist.components.TopRow
import com.gitsoft.thoughtpad.feature.notelist.notelist_sections.all
import com.gitsoft.thoughtpad.feature.notelist.notelist_sections.archived
import com.gitsoft.thoughtpad.feature.notelist.notelist_sections.reminders
import com.gitsoft.thoughtpad.feature.notelist.notelist_sections.trash
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NoteListRoute(
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    viewModel: NoteListViewModel = koinViewModel(),
    onCreateNewNote: (Long?) -> Unit,
    onOpenSettings: () -> Unit,
    onOpenTags: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    NoteListScreen(
        state = state,
        sharedTransitionScope = sharedTransitionScope,
        animatedContentScope = animatedContentScope,
        onCreateNewNote = onCreateNewNote,
        onOpenSettings = onOpenSettings,
        onOpenTags = onOpenTags,
        onToggleNotePin = viewModel::onToggleNotePin,
        onToggleFilterDialog = viewModel::onOpenFilterDialog,
        onToggleSelectedNote = viewModel::onToggleSelectNote,
        onToggleDeleteNote = viewModel::onToggleDelete,
        onToggleArchiveNote = viewModel::onToggleArchive,
        onToggleNoteListType = viewModel::onToggleNoteListType,
        onToggleUnlockNote = viewModel::onToggleUnlockNote,
        onUnlockNotePasswordChange = viewModel::onUnlockNotePasswordChange,
        onTogglePasswordDialog = viewModel::onToggleNotePasswordDialog,
        onUnlockNote = viewModel::onUnlockNote,
        onDismissMessage = viewModel::onDismissMessage
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
internal fun NoteListScreen(
    state: NoteListUiState,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onToggleSelectedNote: (Note?) -> Unit,
    onToggleUnlockNote: (Note) -> Unit,
    onCreateNewNote: (Long?) -> Unit,
    onToggleFilterDialog: (Boolean) -> Unit,
    onOpenSettings: () -> Unit,
    onOpenTags: () -> Unit,
    onToggleNotePin: (Long, Boolean) -> Unit,
    onToggleArchiveNote: (ArchiveState) -> Unit,
    onToggleNoteListType: (NoteListType) -> Unit,
    onToggleDeleteNote: (DeleteState) -> Unit,
    onUnlockNotePasswordChange: (String) -> Unit,
    onTogglePasswordDialog: (Boolean) -> Unit,
    onDismissMessage: (Int) -> Unit,
    onUnlockNote: () -> Unit
) {
    var query: String by rememberSaveable { mutableStateOf("") }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    var selectedDrawerItem: DrawerItem by remember { mutableStateOf(DrawerItem.All) }

    val scope = rememberCoroutineScope()

    val allSearchableNotes by
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

    val trash = allSearchableNotes.filter { it.note.isDeleted }

    val reminders = allSearchableNotes.filter { it.note.reminderTime != null }

    val archivedNotes = allSearchableNotes.filter { it.note.isArchived }

    val allFilteredNotes = allSearchableNotes.filterNot { it.note.isDeleted || it.note.isArchived }

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

    val graphicsLayer = rememberGraphicsLayer()

    DisposableEffect(Unit) {
        LottieCompositionFactory.fromAsset(context, "no_notes.json")
        onDispose { LottieCompositionFactory.clearCache(context) }
    }

    LaunchedEffect(state.userMessages) {
        if (state.userMessages.isNotEmpty()) {
            val userMessage = state.userMessages.first()
            userMessage.message?.let { message ->
                snackbarHostState.showSnackbar(message = message, duration = SnackbarDuration.Short)
                onDismissMessage(userMessage.id)
            }
        }
    }

    LaunchedEffect(state.archiveState) {
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

    LaunchedEffect(state.deleteState) {
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

                Column(modifier = Modifier.fillMaxSize().padding(PaddingValues(horizontal = 16.dp))) {
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
                        onQueryChange = { query = it },
                        selectedNoteListType = state.selectedNoteListType,
                        onToggleNoteList = { onToggleNoteListType(it) }
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
                                modifier = Modifier.testTag(TestTags.NOTE_ACTIONS_BOTTOM_SHEET),
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

                    if (state.unlockDialogIsVisible) {
                        TogaBasicDialog(
                            content = {
                                Box(
                                    modifier =
                                        Modifier.fillMaxWidth(0.8f)
                                            .clip(MaterialTheme.shapes.large)
                                            .background(
                                                color = MaterialTheme.colorScheme.surface,
                                                shape = MaterialTheme.shapes.large
                                            )
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        TogaSmallTitle(
                                            text = stringResource(R.string.unlock_note_dialog_title),
                                            textAlign = TextAlign.Center
                                        )
                                        TogaTextField(
                                            value = state.unlockNotePassword ?: "",
                                            onValueChange = onUnlockNotePasswordChange,
                                            label = R.string.unlock_input_label,
                                            keyboardOptions =
                                                KeyboardOptions(
                                                    imeAction =
                                                        state.unlockNotePassword?.let {
                                                            if (it.length >= 4) ImeAction.Done else ImeAction.Default
                                                        } ?: ImeAction.Default
                                                ),
                                            keyboardActions = KeyboardActions(onDone = { onUnlockNote() }),
                                            minLines = 1,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            TogaTextButton(
                                                text = R.string.cancel,
                                                onClick = { onTogglePasswordDialog(false) }
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            TogaPrimaryButton(
                                                enabled = state.unlockNotePassword?.let { it.trim().length >= 4 } ?: false,
                                                onClick = onUnlockNote
                                            ) {
                                                TogaButtonText(text = stringResource(R.string.unlock))
                                            }
                                        }
                                    }
                                }
                            },
                            onDismissRequest = { onTogglePasswordDialog(false) }
                        )
                    }

                    AnimatedContent(
                        targetState = allSearchableNotes.isEmpty(),
                        label = "Note List Visibility State"
                    ) { isEmpty ->
                        if (isEmpty) {
                            NoNotesIndicator(modifier = Modifier)
                        } else {
                            LazyVerticalStaggeredGrid(
                                state = noteListState,
                                modifier = Modifier.fillMaxSize().testTag(TestTags.NOTE_LIST),
                                columns =
                                    when (state.selectedNoteListType) {
                                        NoteListType.GRID -> StaggeredGridCells.Fixed(2)
                                        NoteListType.LIST -> StaggeredGridCells.Fixed(1)
                                    },
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalItemSpacing = 8.dp
                            ) {
                                when (selectedDrawerItem) {
                                    DrawerItem.All -> {
                                        all(
                                            sharedTransitionScope = sharedTransitionScope,
                                            animatedContentScope = animatedContentScope,
                                            pinnedNotes = pinnedNotes,
                                            allOtherNotes = allOtherNotes,
                                            isDarkTheme = state.isDarkTheme,
                                            selectedNote = state.selectedNote,
                                            graphicsLayer = graphicsLayer,
                                            onOpenDetails = onCreateNewNote,
                                            unlockNote = onToggleUnlockNote,
                                            onToggleSelectedNote = onToggleSelectedNote,
                                            onToggleFilterDialog = onToggleFilterDialog,
                                            unlockedNotes = state.unlockedNotes
                                        )
                                    }
                                    DrawerItem.Archived -> {
                                        archived(
                                            sharedTransitionScope = sharedTransitionScope,
                                            animatedContentScope = animatedContentScope,
                                            archivedNotes = archivedNotes,
                                            isDarkTheme = state.isDarkTheme,
                                            selectedNote = state.selectedNote,
                                            graphicsLayer = graphicsLayer,
                                            onOpenDetails = onCreateNewNote,
                                            unlockNote = onToggleUnlockNote,
                                            onToggleSelectedNote = onToggleSelectedNote,
                                            onToggleFilterDialog = onToggleFilterDialog,
                                            unlockedNotes = state.unlockedNotes
                                        )
                                    }
                                    DrawerItem.Reminders -> {
                                        reminders(
                                            sharedTransitionScope = sharedTransitionScope,
                                            animatedContentScope = animatedContentScope,
                                            reminders = reminders,
                                            reminderDisplayStyle = state.reminderDisplayStyle,
                                            isDarkTheme = state.isDarkTheme,
                                            selectedNote = state.selectedNote,
                                            graphicsLayer = graphicsLayer,
                                            onOpenDetails = onCreateNewNote,
                                            unlockNote = onToggleUnlockNote,
                                            onToggleSelectedNote = onToggleSelectedNote,
                                            onToggleFilterDialog = onToggleFilterDialog,
                                            unlockedNotes = state.unlockedNotes
                                        )
                                    }
                                    DrawerItem.Trash -> {
                                        trash(
                                            sharedTransitionScope = sharedTransitionScope,
                                            animatedContentScope = animatedContentScope,
                                            trash = trash,
                                            isDarkTheme = state.isDarkTheme,
                                            selectedNote = state.selectedNote,
                                            graphicsLayer = graphicsLayer,
                                            onOpenDetails = onCreateNewNote,
                                            unlockNote = onToggleUnlockNote,
                                            onToggleSelectedNote = onToggleSelectedNote,
                                            onToggleFilterDialog = onToggleFilterDialog,
                                            unlockedNotes = state.unlockedNotes
                                        )
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
                        modifier = Modifier.testTag(TestTags.ADD_NOTE_FAB),
                        icon = R.drawable.ic_pen_edit,
                        contentDescription = R.string.add_note,
                        onClick = { onCreateNewNote(null) }
                    )
                }
            }
        }
    }
}

internal object TestTags {
    const val SEARCH_BAR = "search_bar"
    const val SIDEBAR_TOGGLE = "sidebar_toggle"
    const val SIDEBAR = "sidebar"
    const val SIDEBAR_ITEM = "sidebar_item"
    const val SIDEBAR_ITEM_TEXT = "sidebar_item_text"
    const val SIDEBAR_ITEM_ICON = "sidebar_item_icon"
    const val NOTE_LIST = "note_list"
    const val NOTE_ACTIONS_BOTTOM_SHEET = "note_actions_bottom_sheet"
    const val NOTE_ITEM_CARD = "note_item_card"
    const val NOTE_ITEM_CARD_TITLE = "note_item_card_title"
    const val ADD_NOTE_FAB = "add_note_fab"
    const val ALL_NOTES_AND_TASKS_COMPLETED = "all_notes_and_tasks_completed"
    const val NOTE_LIST_TYPE = "note_list_type"
}
