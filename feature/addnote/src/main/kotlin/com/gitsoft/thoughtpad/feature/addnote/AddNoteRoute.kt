
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
package com.gitsoft.thoughtpad.feature.addnote

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.gitsoft.thoughtpad.core.model.CheckListItem
import com.gitsoft.thoughtpad.core.model.NoteColor
import com.gitsoft.thoughtpad.core.model.Tag
import com.gitsoft.thoughtpad.core.model.TagColor
import com.gitsoft.thoughtpad.feature.addnote.components.CheckList
import com.gitsoft.thoughtpad.feature.addnote.components.ColorPill
import com.gitsoft.thoughtpad.feature.addnote.components.NoteColorPicker
import com.gitsoft.thoughtpad.feature.addnote.components.ReminderContent
import com.gitsoft.thoughtpad.feature.addnote.components.ReminderRow
import com.gitsoft.thoughtpad.feature.addnote.components.TagList
import com.gitsoft.thoughtpad.feature.addnote.components.TagSelectionContent
import core.gitsoft.thoughtpad.core.toga.components.appbar.TogaBottomAppBar
import core.gitsoft.thoughtpad.core.toga.components.button.TogaIconButton
import core.gitsoft.thoughtpad.core.toga.components.button.TogaTextButton
import core.gitsoft.thoughtpad.core.toga.components.dialog.TogaDatePickerDialog
import core.gitsoft.thoughtpad.core.toga.components.dialog.TogaTimePickerDialog
import core.gitsoft.thoughtpad.core.toga.components.input.TogaTextField
import core.gitsoft.thoughtpad.core.toga.components.scaffold.TogaStandardScaffold
import core.gitsoft.thoughtpad.core.toga.components.sheets.TogaModalBottomSheet
import core.gitsoft.thoughtpad.core.toga.components.text.TogaLargeLabel
import core.gitsoft.thoughtpad.core.toga.theme.toComposeColor
import java.util.Calendar
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddNoteRoute(onNavigateBack: () -> Unit, viewModel: AddNoteViewModel = koinViewModel()) {
    val state: AddNoteUiState by viewModel.state.collectAsState()
    AddNoteScreen(
        state = state,
        onNavigateBack = onNavigateBack,
        onSave = { viewModel.onEvent(AddNoteEvent.Save) },
        onChangeTitle = { viewModel.onEvent(AddNoteEvent.ChangeTitle(it)) },
        onChangeContent = { viewModel.onEvent(AddNoteEvent.ChangeText(it)) },
        onChangeNoteColor = { viewModel.onEvent(AddNoteEvent.ChangeNoteColor(it)) },
        onChangeTagColor = { viewModel.onEvent(AddNoteEvent.ChangeTagColor(it)) },
        onToggleCheckList = { viewModel.onEvent(AddNoteEvent.ToggleCheckList(it)) },
        onToggleTags = { viewModel.onEvent(AddNoteEvent.ToggleTags(it)) },
        onAddCheckListItem = { viewModel.onEvent(AddNoteEvent.AddCheckListItem(it)) },
        onRemoveCheckListItem = { viewModel.onEvent(AddNoteEvent.RemoveCheckListItem(it)) },
        onToggleTagSelection = { tag -> viewModel.onEvent(AddNoteEvent.ToggleTagSelection(tag)) },
        onRemoveTag = { viewModel.onEvent(AddNoteEvent.RemoveTag(it)) },
        onToggleColorBar = { viewModel.onEvent(AddNoteEvent.ToggleColorBar(it)) },
        onTogglePin = { viewModel.onEvent(AddNoteEvent.TogglePin(it)) },
        onToggleDateDialog = { viewModel.onEvent(AddNoteEvent.ToggleDateDialog(it)) },
        onToggleReminder = { viewModel.onEvent(AddNoteEvent.ToggleReminders(it)) },
        onCheckListItemCheckedChange = { item, checked ->
            viewModel.onEvent(AddNoteEvent.CheckListItemCheckedChange(item, checked))
        },
        onAddNewTag = { viewModel.onEvent(AddNoteEvent.AddTag(it)) },
        onToggleTagSheet = { viewModel.onEvent(AddNoteEvent.ToggleTagSheet(it)) },
        onToggleTimeDialog = { viewModel.onEvent(AddNoteEvent.ToggleTimeDialog(it)) },
        onChangeDate = { viewModel.onEvent(AddNoteEvent.ChangeDate(it)) },
        onChangeTime = { viewModel.onEvent(AddNoteEvent.ChangeTime(it)) },
        onToggleDateSheet = { viewModel.onEvent(AddNoteEvent.ToggleDateSheet(it)) },
        onDiscardNote = { viewModel.onEvent(AddNoteEvent.DiscardNote) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddNoteScreen(
    state: AddNoteUiState,
    onNavigateBack: () -> Unit,
    onSave: () -> Unit,
    onChangeTitle: (String) -> Unit,
    onChangeContent: (String) -> Unit,
    onToggleColorBar: (Boolean) -> Unit,
    onChangeNoteColor: (NoteColor) -> Unit,
    onChangeTagColor: (TagColor) -> Unit,
    onToggleCheckList: (Boolean) -> Unit,
    onToggleTags: (Boolean) -> Unit,
    onAddCheckListItem: (CheckListItem) -> Unit,
    onRemoveCheckListItem: (CheckListItem) -> Unit,
    onToggleTagSelection: (Tag) -> Unit,
    onRemoveTag: (Tag) -> Unit,
    onAddNewTag: (Tag) -> Unit,
    onTogglePin: (Boolean) -> Unit,
    onToggleReminder: (Boolean) -> Unit,
    onChangeDate: (Long) -> Unit,
    onChangeTime: (Long) -> Unit,
    onDiscardNote: () -> Unit,
    onToggleDateDialog: (Boolean) -> Unit,
    onToggleTimeDialog: (Boolean) -> Unit,
    onToggleTagSheet: (Boolean) -> Unit,
    onToggleDateSheet: (Boolean) -> Unit,
    onCheckListItemCheckedChange: (CheckListItem, Boolean) -> Unit
) {
    val navigateToNoteList by rememberUpdatedState(onNavigateBack)

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.insertionSuccessful) {
        if (state.insertionSuccessful) {
            navigateToNoteList()
        }
    }

    val context = LocalContext.current

    LaunchedEffect(state.deletedSuccessfully) {
        if (state.deletedSuccessfully) {
            Toast.makeText(context, context.getString(R.string.empty_note_discarded), Toast.LENGTH_LONG)
                .show()
            navigateToNoteList()
        }
    }

    if (state.timeDialogIsVisible) {
        state.selectedDate?.let {
            TogaTimePickerDialog(
                selectedDate = state.selectedDate,
                onConfirm = { timePickerState ->
                    val selectedTimeMillis = getTimeInMillisFromPicker(timePickerState)
                    onChangeTime(selectedTimeMillis) // Pass the calculated timestamp
                    onToggleTimeDialog(false)
                },
                onDismiss = { onToggleTimeDialog(false) }
            )
        }
    }

    if (state.dateDialogIsVisible) {
        TogaDatePickerDialog(
            onDateSelected = {
                onChangeDate(it ?: return@TogaDatePickerDialog)
                onToggleDateDialog(false)
            },
            onDismiss = { onToggleDateDialog(false) }
        )
    }

    if (state.isTagSheetVisible) {
        TogaModalBottomSheet(onDismissRequest = { onToggleTagSheet(false) }) {
            TagSelectionContent(
                isSystemInDarkTheme = state.systemInDarkMode,
                existingTags = state.defaultTags,
                selectedTags = state.selectedTags,
                onToggleTagSelection = onToggleTagSelection,
                onNewTagAdded = onAddNewTag,
                onDismissRequest = { onToggleTagSheet(false) },
                selectedTagColor = state.selectedTagColor,
                tagColors = state.tagColors,
                onChangeTagColor = onChangeTagColor
            )
        }
    }

    /** check if user has entered any text and pressed back button before they could save. */
    BackHandler(enabled = true) {
        if (state.noteIsValid) {
            onSave()
        } else {
            onDiscardNote()
        }
    }

    TogaStandardScaffold(
        snackbarHostState = snackbarHostState,
        onNavigateBack = {
            if (state.noteIsValid) {
                onSave()
            } else {
                onDiscardNote()
            }
        },
        actions = {
            TogaIconButton(
                icon = if (state.note.isPinned) R.drawable.ic_pin_filled else R.drawable.ic_pin,
                contentDescription = R.string.pin,
                tint =
                    if (state.note.isPinned) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onBackground
                    },
                onClick = { onTogglePin(!state.note.isPinned) }
            )
            TogaTextButton(text = R.string.save, enabled = state.noteIsValid, onClick = onSave)
        },
        bottomBar = {
            TogaBottomAppBar(
                containerColor =
                    if (state.systemInDarkMode) {
                        state.selectedNoteColor.darkColor.toComposeColor()
                    } else state.selectedNoteColor.lightColor.toComposeColor(),
                actions = {
                    TogaIconButton(
                        icon = R.drawable.ic_paint,
                        onClick = { onToggleColorBar(!state.isColorVisible) },
                        contentDescription = R.string.color_bar
                    )

                    TogaIconButton(
                        icon =
                            if (!state.hasReminder) {
                                R.drawable.ic_notifications_outlined
                            } else R.drawable.ic_notifications_filled,
                        onClick = { onToggleReminder(!state.hasReminder) },
                        contentDescription = R.string.toggle_reminder,
                        tint =
                            if (!state.hasReminder) {
                                MaterialTheme.colorScheme.onBackground
                            } else MaterialTheme.colorScheme.primary
                    )
                }
            )
        },
        appBarColors =
            TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor =
                    if (state.systemInDarkMode) {
                        state.selectedNoteColor.darkColor.toComposeColor()
                    } else state.selectedNoteColor.lightColor.toComposeColor()
            )
    ) { innerPadding ->
        if (state.isColorVisible) {
            TogaModalBottomSheet(onDismissRequest = { onToggleColorBar(false) }) {
                NoteColorPicker(
                    modifier = Modifier.padding(PaddingValues(16.dp)),
                    isDarkTheme = state.systemInDarkMode,
                    selectedColor = state.selectedNoteColor,
                    colors = state.noteColors,
                    onChangeColor = onChangeNoteColor,
                    onDismissRequest = { onToggleColorBar(false) }
                )
            }
        }

        if (state.isDateSheetVisible) {
            TogaModalBottomSheet(
                onDismissRequest = { onToggleDateSheet(false) },
                content = {
                    ReminderContent(
                        onDateSelected = {
                            if (it != null) {
                                onChangeDate(it)
                                onChangeTime(it)
                            } else {
                                onToggleDateDialog(true)
                            }
                            onToggleDateSheet(false)
                        }
                    )
                }
            )
        }

        LazyColumn(
            modifier =
                Modifier.fillMaxSize()
                    .padding(innerPadding)
                    .background(
                        if (state.systemInDarkMode) {
                            state.selectedNoteColor.darkColor.toComposeColor()
                        } else state.selectedNoteColor.lightColor.toComposeColor()
                    ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                TogaTextField(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    value = state.note.noteTitle ?: "",
                    onValueChange = onChangeTitle,
                    textStyle = MaterialTheme.typography.titleMedium,
                    label = R.string.note_title,
                    minLines = 1
                )
            }
            item {
                TogaTextField(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    value = state.note.noteText ?: "",
                    onValueChange = onChangeContent,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    label = R.string.note_content
                )
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(checked = state.note.isCheckList, onCheckedChange = onToggleCheckList)
                    TogaLargeLabel(R.string.has_checklist)
                }
            }
            item {
                AnimatedVisibility(visible = state.note.isCheckList) {
                    CheckList(
                        checklistItems = state.checkListItems,
                        onCheckedChange = onCheckListItemCheckedChange,
                        onAddItem = onAddCheckListItem,
                        onDeleteItem = onRemoveCheckListItem
                    )
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(checked = state.hasTags, onCheckedChange = onToggleTags)
                    TogaLargeLabel(R.string.has_tags, modifier = Modifier.weight(1f))
                    AnimatedVisibility(visible = state.hasTags, modifier = Modifier) {
                        TogaIconButton(
                            icon = R.drawable.ic_add,
                            onClick = { onToggleTagSheet(true) },
                            contentDescription = R.string.add_tag
                        )
                    }
                }
            }
            item {
                AnimatedVisibility(
                    modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 16.dp),
                    visible = state.hasTags,
                    enter = fadeIn(initialAlpha = 0.3f),
                    exit = fadeOut(targetAlpha = 0.3f)
                ) {
                    TagList(
                        isSystemInDarkTheme = state.systemInDarkMode,
                        tags = state.selectedTags,
                        onDeleteTag = onRemoveTag
                    )
                }
            }
            item {
                AnimatedVisibility(
                    modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 16.dp),
                    visible = state.hasReminder && state.selectedDate != null,
                    enter = scaleIn() + slideInVertically(),
                    exit = scaleOut() + slideOutVertically()
                ) {
                    state.selectedDate?.let {
                        ReminderRow(
                            reminderTime = state.selectedDate,
                            onChangeDate = onToggleDateDialog,
                            onChangeTime = onToggleTimeDialog
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TagColorPicker(
    isDarkTheme: Boolean,
    selectedColor: TagColor,
    colors: List<TagColor>,
    onColorSelected: (TagColor) -> Unit
) {
    LazyRow(
        modifier = Modifier.padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = colors, key = { it.ordinal }) { colorValue ->
            ColorPill(
                color =
                    if (isDarkTheme) {
                        colorValue.darkColor.toComposeColor()
                    } else colorValue.lightColor.toComposeColor(),
                isSelected = colorValue == selectedColor,
                onSelect = {
                    onColorSelected(
                        TagColor.entries.find { tagColor ->
                            tagColor.darkColor.toComposeColor() == it ||
                                tagColor.lightColor.toComposeColor() == it
                        } ?: TagColor.Blue
                    )
                }
            )
        }
    }
}

// Helper function to convert TimePickerState to a timestamp (Long)
@OptIn(ExperimentalMaterial3Api::class)
fun getTimeInMillisFromPicker(timePickerState: TimePickerState): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
    calendar.set(Calendar.MINUTE, timePickerState.minute)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    // Return the timestamp in milliseconds
    return calendar.timeInMillis
}
