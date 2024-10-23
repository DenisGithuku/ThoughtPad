
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

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gitsoft.thoughtpad.core.model.CheckListItem
import com.gitsoft.thoughtpad.core.model.Tag
import com.gitsoft.thoughtpad.feature.addnote.components.CheckList
import com.gitsoft.thoughtpad.feature.addnote.components.ColorPill
import com.gitsoft.thoughtpad.feature.addnote.components.NoteColorPicker
import com.gitsoft.thoughtpad.feature.addnote.components.ReminderRow
import com.gitsoft.thoughtpad.feature.addnote.components.TagList
import core.gitsoft.thoughtpad.core.toga.components.appbar.TogaBottomAppBar
import core.gitsoft.thoughtpad.core.toga.components.button.TogaIconButton
import core.gitsoft.thoughtpad.core.toga.components.button.TogaPrimaryButton
import core.gitsoft.thoughtpad.core.toga.components.button.TogaTextButton
import core.gitsoft.thoughtpad.core.toga.components.chips.TogaInputChip
import core.gitsoft.thoughtpad.core.toga.components.dialog.TogaDatePickerDialog
import core.gitsoft.thoughtpad.core.toga.components.dialog.TogaTimePickerDialog
import core.gitsoft.thoughtpad.core.toga.components.input.TogaTextField
import core.gitsoft.thoughtpad.core.toga.components.scaffold.TogaStandardScaffold
import core.gitsoft.thoughtpad.core.toga.components.text.TogaButtonText
import core.gitsoft.thoughtpad.core.toga.components.text.TogaDefaultText
import core.gitsoft.thoughtpad.core.toga.components.text.TogaLargeLabel
import core.gitsoft.thoughtpad.core.toga.theme.toComposeLong
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
        onChangeTime = { viewModel.onEvent(AddNoteEvent.ChangeTime(it)) }
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
    onChangeNoteColor: (Color) -> Unit,
    onChangeTagColor: (Color) -> Unit,
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
    onToggleDateDialog: (Boolean) -> Unit,
    onToggleTimeDialog: (Boolean) -> Unit,
    onToggleTagSheet: (Boolean) -> Unit,
    onCheckListItemCheckedChange: (CheckListItem, Boolean) -> Unit
) {
    TogaStandardScaffold(
        onNavigateBack = onNavigateBack,
        title = R.string.add_note,
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
                containerColor = state.selectedNoteColor,
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
            TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = state.selectedNoteColor)
    ) { innerPadding ->
        if (state.timeDialogIsVisible) {
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
            ModalBottomSheet(onDismissRequest = { onToggleTagSheet(false) }) {
                TagSelectionBottomSheet(
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

        val navigateToNoteList by rememberUpdatedState(onNavigateBack)

        LaunchedEffect(state.insertionSuccessful) {
            if (state.insertionSuccessful) {
                navigateToNoteList()
            }
        }

        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            LazyColumn(
                modifier =
                    Modifier.matchParentSize().align(Alignment.TopStart).background(state.selectedNoteColor),
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
                        enter = scaleIn(initialScale = 0.5f) + fadeIn(initialAlpha = 0.3f),
                        exit = scaleOut(targetScale = 0.5f) + fadeOut(targetAlpha = 0.3f)
                    ) {
                        TagList(tags = state.selectedTags, onDeleteTag = onRemoveTag)
                    }
                }
                item {
                    AnimatedVisibility(
                        modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 16.dp),
                        visible = state.hasReminder,
                        enter = scaleIn() + slideInVertically(),
                        exit = scaleOut() + slideOutVertically()
                    ) {
                        ReminderRow(
                            reminderTime = state.selectedDate,
                            onChangeDate = onToggleDateDialog,
                            onChangeTime = onToggleTimeDialog
                        )
                    }
                }
            }
            AnimatedVisibility(
                visible = state.isColorVisible,
                modifier = Modifier.align(Alignment.BottomCenter),
                enter = scaleIn() + slideInVertically(),
                exit = scaleOut() + slideOutVertically()
            ) {
                NoteColorPicker(
                    selectedColor = state.selectedNoteColor,
                    colors = state.noteColors,
                    onChangeColor = onChangeNoteColor
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagSelectionBottomSheet(
    existingTags: List<Tag>,
    selectedTags: List<Tag>,
    selectedTagColor: Color,
    tagColors: List<Color>,
    onToggleTagSelection: (Tag) -> Unit,
    onNewTagAdded: (Tag) -> Unit,
    onChangeTagColor: (Color) -> Unit,
    onDismissRequest: () -> Unit
) {
    var tagNameState by rememberSaveable { mutableStateOf("") }
    val context: Context = LocalContext.current

    val tagIsValid =
        remember(tagNameState) {
            derivedStateOf {
                tagNameState.isNotBlank() && existingTags.none { it.name.equals(tagNameState, true) }
            }
        }

    Column(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.weight(1f))
            TogaDefaultText("Select Tags", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.weight(1f))
            TogaIconButton(
                icon = R.drawable.ic_close,
                onClick = onDismissRequest,
                contentDescription = R.string.close
            )
        }

        // Show default tags
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            existingTags.forEach { tag ->
                TogaInputChip(
                    text = tag.name ?: "",
                    isSelected = selectedTags.any { it isTheSameAs tag },
                    onSelectChanged = { onToggleTagSelection(tag) },
                    color = tag.color
                )
            }
        }

        // New tag input
        TogaTextField(
            value = tagNameState,
            onValueChange = { input -> tagNameState = input },
            label = R.string.add_new_tag,
            modifier = Modifier.fillMaxWidth()
        )

        // Tag colors
        AnimatedVisibility(
            visible = tagIsValid.value,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            TagColorPicker(
                selectedColor = selectedTagColor,
                colors = tagColors,
                onColorSelected = onChangeTagColor
            )
        }

        // Add button
        TogaPrimaryButton(
            enabled = tagIsValid.value,
            onClick = {
                val newTag = Tag(name = tagNameState, color = selectedTagColor.toComposeLong())
                onNewTagAdded(newTag)
                onChangeTagColor(tagColors.first())
                tagNameState = ""
                Toast.makeText(context, context.getString(R.string.new_tag_added), Toast.LENGTH_SHORT)
                    .show()
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            TogaButtonText(text = stringResource(id = R.string.add_tag))
        }
    }
}

@Composable
fun TagColorPicker(selectedColor: Color, colors: List<Color>, onColorSelected: (Color) -> Unit) {
    LazyRow(
        modifier = Modifier.padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = colors, key = { it.value.toLong() }) { colorValue ->
            ColorPill(
                color = colorValue,
                isSelected = colorValue == selectedColor,
                onSelect = onColorSelected
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

// Helper function for checking if two tags are the same
infix fun Tag.isTheSameAs(other: Tag): Boolean = this.name.equals(other.name, true)
