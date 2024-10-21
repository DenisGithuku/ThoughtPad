
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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.gitsoft.thoughtpad.core.model.CheckListItem
import com.gitsoft.thoughtpad.core.model.Tag
import core.gitsoft.thoughtpad.core.toga.components.appbar.TogaBottomAppBar
import core.gitsoft.thoughtpad.core.toga.components.button.TogaIconButton
import core.gitsoft.thoughtpad.core.toga.components.button.TogaTextButton
import core.gitsoft.thoughtpad.core.toga.components.dialog.TogaTimePickerDialog
import core.gitsoft.thoughtpad.core.toga.components.input.TogaTextField
import core.gitsoft.thoughtpad.core.toga.components.scaffold.TogaStandardScaffold
import core.gitsoft.thoughtpad.core.toga.components.text.TogaDefaultText
import core.gitsoft.thoughtpad.core.toga.components.text.TogaLargeLabel
import core.gitsoft.thoughtpad.core.toga.components.text.TogaMediumLabel
import java.util.Calendar
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddNoteRoute(onNavigateBack: () -> Unit, viewModel: AddNoteViewModel = koinViewModel()) {
    val state: AddNoteUiState by viewModel.state.collectAsState()
    AddNoteScreen(
        state = state,
        onNavigateBack = onNavigateBack,
        onSave = { viewModel.onEvent(AddNoteEvent.Save) },
        onChangeTitle = { viewModel.onEvent(AddNoteEvent.ChangeNoteTitle(it)) },
        onChangeContent = { viewModel.onEvent(AddNoteEvent.ChangeNoteText(it)) },
        onChangeColor = { viewModel.onEvent(AddNoteEvent.ChangeNoteColor(it)) },
        onToggleCheckList = { viewModel.onEvent(AddNoteEvent.ToggleNoteCheckList(it)) },
        onToggleTags = { viewModel.onEvent(AddNoteEvent.ToggleNoteTags(it)) },
        onAddCheckListItem = { viewModel.onEvent(AddNoteEvent.AddNoteCheckListItem(it)) },
        onRemoveCheckListItem = { viewModel.onEvent(AddNoteEvent.RemoveCheckListItem(it)) },
        onAddTag = { viewModel.onEvent(AddNoteEvent.AddNoteTag(it)) },
        onRemoveTag = { viewModel.onEvent(AddNoteEvent.RemoveTag(it)) },
        onToggleColorBar = { viewModel.onEvent(AddNoteEvent.ToggleColorBar(it)) },
        onTogglePin = { viewModel.onEvent(AddNoteEvent.TogglePin(it)) },
        onToggleReminderDialog = { viewModel.onEvent(AddNoteEvent.ToggleReminders(it)) },
        onChangeReminder = { viewModel.onEvent(AddNoteEvent.ChangeReminder(it)) },
        onCheckListItemCheckedChange = { item, checked ->
            viewModel.onEvent(AddNoteEvent.CheckListItemCheckedChange(item, checked))
        }
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
    onChangeColor: (Color) -> Unit,
    onToggleCheckList: (Boolean) -> Unit,
    onToggleTags: (Boolean) -> Unit,
    onAddCheckListItem: (CheckListItem) -> Unit,
    onRemoveCheckListItem: (CheckListItem) -> Unit,
    onAddTag: (Tag) -> Unit,
    onRemoveTag: (Tag) -> Unit,
    onTogglePin: (Boolean) -> Unit,
    onChangeReminder: (Long?) -> Unit,
    onToggleReminderDialog: (Boolean) -> Unit,
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
                containerColor = state.selectedColor,
                actions = {
                    TogaIconButton(
                        icon = R.drawable.ic_paint,
                        onClick = { onToggleColorBar(!state.isColorVisible) },
                        contentDescription = R.string.color_bar
                    )

                    TogaIconButton(
                        icon =
                            if (state.note.reminderTime == null) {
                                R.drawable.ic_notifications_outlined
                            } else R.drawable.ic_notifications_filled,
                        onClick = { onToggleReminderDialog(true) },
                        contentDescription = R.string.toggle_reminder,
                        tint =
                            if (state.note.reminderTime == null) {
                                MaterialTheme.colorScheme.onBackground
                            } else MaterialTheme.colorScheme.primary
                    )
                }
            )
        },
        appBarColors =
            TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = state.selectedColor)
    ) { innerPadding ->
        if (state.isReminderDialogVisible) {
            TogaTimePickerDialog(
                onConfirm = { timePickerState ->
                    val selectedTimeMillis = getTimeInMillisFromPicker(timePickerState)
                    onChangeReminder(selectedTimeMillis) // Pass the calculated timestamp
                    onToggleReminderDialog(false)
                },
                onDismiss = {
                    onChangeReminder(null)
                    onToggleReminderDialog(false)
                }
            )
        }

        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            LazyColumn(
                modifier =
                    Modifier.matchParentSize().align(Alignment.TopStart).background(state.selectedColor),
                verticalArrangement = Arrangement.spacedBy(8.dp)
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
                        TogaLargeLabel(R.string.has_tags)
                    }
                }
                item {
                    AnimatedVisibility(visible = state.hasTags) {
                        TagList(
                            tags = state.tags,
                            onAddTag = onAddTag,
                            onDeleteTag = onRemoveTag,
                            onSelectTagColor = {}
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
                ColorRow(
                    selectedColor = state.selectedColor,
                    colors = state.colors,
                    onChangeColor = onChangeColor
                )
            }
        }
    }
}

@Composable
fun ColorRow(
    modifier: Modifier = Modifier,
    selectedColor: Color,
    colors: List<Color>,
    onChangeColor: (Color) -> Unit
) {
    LazyRow(
        modifier = modifier.fillMaxWidth().padding(PaddingValues(horizontal = 16.dp, vertical = 8.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = colors) {
            ColorPill(color = it, isSelected = it == selectedColor, onSelect = onChangeColor)
        }
    }
}

@Composable
fun ColorPill(color: Color, isSelected: Boolean, onSelect: (Color) -> Unit) {
    val borderColor by
        animateColorAsState(
            if (isSelected) {
                MaterialTheme.colorScheme.onBackground
            } else {
                Color.Transparent
            }
        )
    Box(
        modifier =
            Modifier.size(40.dp)
                .clip(CircleShape)
                .border(width = 3.dp, color = borderColor, shape = CircleShape)
                .background(color)
                .clickable { onSelect(color) }
    )
}

@Composable
fun CheckList(
    checklistItems: List<CheckListItem>,
    onCheckedChange: (CheckListItem, Boolean) -> Unit, // Handles check/uncheck
    onAddItem: (CheckListItem) -> Unit, // Handles adding a new checklist item
    onDeleteItem: (CheckListItem) -> Unit // Handles item deletion
) {
    Column {
        // Display existing checklist items
        checklistItems.forEach { item ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = item.isChecked,
                    onCheckedChange = { checked -> onCheckedChange(item, checked) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                TogaMediumLabel(
                    text = item.text ?: "",
                    modifier = Modifier.weight(1f) // Ensures the text takes the remaining space
                )
                IconButton(onClick = { onDeleteItem(item) }) {
                    Icon(painter = painterResource(id = R.drawable.ic_delete), contentDescription = "Delete")
                }
            }
        }

        // TextField to add new checklist items
        var newItemText by remember { mutableStateOf("") }
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TogaTextField(
                value = newItemText,
                onValueChange = { newItemText = it },
                label = R.string.add_new_item,
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions =
                    KeyboardActions(
                        onDone = {
                            if (newItemText.isNotEmpty()) {
                                onAddItem(CheckListItem(text = newItemText))
                                newItemText = ""
                            }
                        }
                    ) // Takes up the rest of the row
            )
            IconButton(
                onClick = {
                    if (newItemText.isNotEmpty()) {
                        onAddItem(CheckListItem(text = newItemText))
                        newItemText = ""
                    }
                }
            ) {
                Icon(painter = painterResource(id = R.drawable.ic_add), contentDescription = "Add Item")
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagList(
    tags: List<Tag>,
    onAddTag: (Tag) -> Unit, // Handle adding a tag with a selected color
    onDeleteTag: (Tag) -> Unit, // Handle tag deletion
    onSelectTagColor: (Tag) -> Unit // Handle changing tag color
) {
    Column {
        // Display existing tags
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp), // Space between items
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tags.forEach { tag ->
                TagChip(
                    tag = tag,
                    onDelete = { onDeleteTag(tag) },
                    onSelectColor = { onSelectTagColor(tag) }
                )
            }
        }

        // TextField to add new tags
        var newTagText by remember { mutableStateOf("") }
        var selectedColor by remember { mutableStateOf(0xFFFFFFFF) } // Default color

        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TogaTextField(
                value = newTagText,
                onValueChange = { newTagText = it },
                label = R.string.add_new_tag,
                modifier = Modifier.weight(1f)
            )
            ColorPicker(
                selectedColor = selectedColor,
                onColorSelected = { color -> selectedColor = color }
            )
            IconButton(
                onClick = {
                    if (newTagText.isNotEmpty()) {
                        onAddTag(Tag(name = newTagText, color = selectedColor))
                        newTagText = ""
                    }
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = stringResource(R.string.add_tag)
                )
            }
        }
    }
}

@Composable
fun TagChip(tag: Tag, onDelete: () -> Unit, onSelectColor: () -> Unit) {
    Row(
        modifier =
            Modifier.clip(CircleShape).background(Color(tag.color ?: return)).padding(8.dp).clickable {
                onSelectColor()
            }, // Click to change color
        verticalAlignment = Alignment.CenterVertically
    ) {
        TogaDefaultText(
            text = tag.name ?: "",
            color = Color.White,
            modifier = Modifier.padding(end = 8.dp)
        )
        IconButton(onClick = { onDelete() }) {
            Icon(painter = painterResource(id = R.drawable.ic_close), contentDescription = "Delete")
        }
    }
}

@Composable
fun ColorPicker(selectedColor: Long, onColorSelected: (Long) -> Unit) {
    // Display a row of selectable colors (for simplicity, use fixed colors)
    val colors = listOf(0xFFF44336, 0xFF2196F3, 0xFF4CAF50, 0xFFFFEB3B)

    Row(
        modifier = Modifier.padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        colors.forEach { colorValue ->
            Box(
                modifier =
                    Modifier.size(32.dp)
                        .clip(CircleShape)
                        .background(Color(colorValue))
                        .clickable { onColorSelected(colorValue) }
                        .border(width = if (colorValue == selectedColor) 2.dp else 0.dp, color = Color.Black)
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
