
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

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gitsoft.thoughtpad.core.model.CheckListItem
import com.gitsoft.thoughtpad.core.model.NoteColor
import com.gitsoft.thoughtpad.core.model.Tag
import com.gitsoft.thoughtpad.core.model.TagColor
import com.gitsoft.thoughtpad.core.toga.components.appbar.TogaBottomAppBar
import com.gitsoft.thoughtpad.core.toga.components.button.TogaIconButton
import com.gitsoft.thoughtpad.core.toga.components.button.TogaTextButton
import com.gitsoft.thoughtpad.core.toga.components.dialog.TogaDatePickerDialog
import com.gitsoft.thoughtpad.core.toga.components.dialog.TogaTimePickerDialog
import com.gitsoft.thoughtpad.core.toga.components.input.TogaTextField
import com.gitsoft.thoughtpad.core.toga.components.scaffold.TogaStandardScaffold
import com.gitsoft.thoughtpad.core.toga.components.sheets.TogaModalBottomSheet
import com.gitsoft.thoughtpad.core.toga.components.text.TogaLargeLabel
import com.gitsoft.thoughtpad.core.toga.components.text.TogaSmallBody
import com.gitsoft.thoughtpad.core.toga.theme.toComposeColor
import com.gitsoft.thoughtpad.feature.addnote.components.CheckList
import com.gitsoft.thoughtpad.feature.addnote.components.ColorPill
import com.gitsoft.thoughtpad.feature.addnote.components.NoteColorPicker
import com.gitsoft.thoughtpad.feature.addnote.components.ReminderContent
import com.gitsoft.thoughtpad.feature.addnote.components.ReminderRow
import com.gitsoft.thoughtpad.feature.addnote.components.SecureNoteContent
import com.gitsoft.thoughtpad.feature.addnote.components.TagList
import com.gitsoft.thoughtpad.feature.addnote.components.TagSelectionContent
import com.gitsoft.thoughtpad.feature.addnote.util.formatTimeAgo
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import java.util.Calendar
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AddNoteRoute(
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onNavigateBack: () -> Unit,
    viewModel: AddNoteViewModel = koinViewModel()
) {
    val state: AddNoteUiState by viewModel.state.collectAsState()
    AddNoteScreen(
        state = state,
        sharedTransitionScope = sharedTransitionScope,
        animatedContentScope = animatedContentScope,
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
        updatePermissionsStatus = { viewModel.onEvent(AddNoteEvent.UpdateNotificationPermissions) },
        onToggleDateSheet = { viewModel.onEvent(AddNoteEvent.ToggleDateSheet(it)) },
        onDiscardNote = { viewModel.onEvent(AddNoteEvent.DiscardNote) },
        onTogglePasswordSheet = { viewModel.onEvent(AddNoteEvent.TogglePasswordDialog(it)) },
        onPasswordChange = { viewModel.onEvent(AddNoteEvent.ChangePassword(it)) },
        onRemovePassword = { viewModel.onEvent(AddNoteEvent.RemovePassword) },
        onSecureNote = { viewModel.onEvent(AddNoteEvent.SecureNote) }
    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalPermissionsApi::class,
    ExperimentalLayoutApi::class,
    ExperimentalSharedTransitionApi::class
)
@Composable
internal fun AddNoteScreen(
    state: AddNoteUiState,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
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
    updatePermissionsStatus: () -> Unit,
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
    onCheckListItemCheckedChange: (CheckListItem, Boolean) -> Unit,
    onTogglePasswordSheet: (Boolean) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRemovePassword: () -> Unit,
    onSecureNote: () -> Unit,
) {
    val navigateToNoteList by rememberUpdatedState(onNavigateBack)

    val snackbarHostState = remember { SnackbarHostState() }

    val scope = rememberCoroutineScope()

    LaunchedEffect(state.insertionSuccessful) {
        if (state.insertionSuccessful) {
            navigateToNoteList()
        }
    }

    val context = LocalContext.current

    val notificationPermissionsState =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            object : PermissionState {
                override val permission: String
                    get() = "android.permission.POST_NOTIFICATIONS"

                override val status: PermissionStatus
                    get() = PermissionStatus.Granted

                override fun launchPermissionRequest() {
                    // Not needed for devices lower than TIRAMISU
                }
            }
        }

    LaunchedEffect(notificationPermissionsState.status) {
        if (
            notificationPermissionsState.status.isGranted ||
                Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU
        ) {
            updatePermissionsStatus()
        }
    }

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
                modifier = Modifier.testTag(TestTags.TIME_DIALOG),
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
            modifier = Modifier.testTag(TestTags.DATE_DIALOG),
            onDateSelected = {
                onChangeDate(it ?: return@TogaDatePickerDialog)
                onToggleDateDialog(false)
            },
            onDismiss = { onToggleDateDialog(false) }
        )
    }

    if (state.isTagSheetVisible) {
        TogaModalBottomSheet(
            modifier = Modifier.testTag(TestTags.TAG_BOTTOM_SHEET),
            onDismissRequest = { onToggleTagSheet(false) }
        ) {
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

    if (state.isColorVisible) {
        TogaModalBottomSheet(
            modifier = Modifier.testTag(TestTags.NOTE_COLOR_PICKER),
            onDismissRequest = { onToggleColorBar(false) }
        ) {
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
            modifier = Modifier.testTag(TestTags.REMINDER_BOTTOM_SHEET),
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

    if (state.isPasswordSheetVisible) {
        TogaModalBottomSheet(
            modifier = Modifier.testTag(TestTags.PASSWORD_SHEET),
            onDismissRequest = { onTogglePasswordSheet(false) }
        ) {
            SecureNoteContent(
                password = state.password ?: "",
                onPasswordChange = onPasswordChange,
                onSecureNote = onSecureNote,
                onDismissRequest = { onTogglePasswordSheet(false) }
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
                modifier = Modifier.testTag(TestTags.TOGGLE_PIN_BUTTON),
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
            TogaIconButton(
                modifier = Modifier.testTag(TestTags.SECURE_BUTTON),
                icon = if(state.encryptedPassword != null) R.drawable.ic_lock_filled else R.drawable.ic_unlock,
                tint = if(state.encryptedPassword != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                onClick = {
                    if (state.encryptedPassword != null) {
                        onRemovePassword()
                    } else {
                        onTogglePasswordSheet(!state.isPasswordSheetVisible)
                    }
                },
                contentDescription = if (state.password.isNullOrEmpty()) {
                    R.string.lock_note
                } else {
                    R.string.unlock_note
                },
            )
            TogaTextButton(
                modifier = Modifier.testTag(TestTags.SAVE_BUTTON),
                text = R.string.save,
                enabled = state.noteIsValid,
                onClick = onSave
            )
        },
        bottomBar = {
            TogaBottomAppBar(
                modifier = Modifier.navigationBarsPadding(),
                containerColor =
                    if (state.systemInDarkMode) {
                        state.selectedNoteColor.darkColor.toComposeColor()
                    } else state.selectedNoteColor.lightColor.toComposeColor(),
                actions = {
                    TogaIconButton(
                        modifier = Modifier.testTag(TestTags.TOGGLE_COLOR_BUTTON),
                        icon = R.drawable.ic_paint,
                        onClick = { onToggleColorBar(!state.isColorVisible) },
                        contentDescription = R.string.color_bar
                    )

                    TogaIconButton(
                        modifier = Modifier.testTag(TestTags.TOGGLE_REMINDER_BUTTON),
                        icon =
                            if (!state.hasReminder) {
                                R.drawable.ic_notifications_outlined
                            } else R.drawable.ic_notifications_filled,
                        onClick = {
                            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                            // Check for Notification Permission on Tiramisu and above
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                if (!notificationPermissionsState.status.isGranted) {
                                    // Request notification permission if not granted
                                    notificationPermissionsState.launchPermissionRequest()

                                    // After granting notification permission, proceed to check alarm permission
                                    if (notificationPermissionsState.status.isGranted) {
                                        // Check and request alarm permission next if needed
                                        checkAndRequestAlarmPermission(alarmManager, context)
                                    }

                                    return@TogaIconButton
                                } else if (notificationPermissionsState.status.shouldShowRationale) {
                                    // Show rationale if notification permission was denied and needs explanation
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = context.getString(R.string.permission_required),
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                    return@TogaIconButton
                                }
                            }

                            // If notification permission is already granted, check alarm permission directly
                            if (
                                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                                    !alarmManager.canScheduleExactAlarms()
                            ) {
                                // Show Snackbar to inform user of alarm permission requirement
                                scope.launch {
                                    val result =
                                        snackbarHostState.showSnackbar(
                                            message = context.getString(R.string.alarm_permissions_required),
                                            actionLabel = context.getString(R.string.allow)
                                        )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        requestAlarmPermission(context)
                                    }
                                }
                            } else {
                                // If all permissions are granted, toggle the reminder
                                onToggleReminder(!state.hasReminder)
                            }
                        },
                        contentDescription = R.string.toggle_reminder,
                        tint =
                            if (!state.hasReminder) {
                                MaterialTheme.colorScheme.onBackground
                            } else MaterialTheme.colorScheme.primary
                    )
                    TogaSmallBody(
                        text =
                            buildString {
                                append(stringResource(R.string.edited))
                                append(formatTimeAgo(state.note.updatedAt ?: System.currentTimeMillis()))
                            }
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
        with(sharedTransitionScope) {
            LazyColumn(
                modifier =
                Modifier
                    .fillMaxSize()
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
                        modifier =
                        Modifier
                            .sharedElement(
                                state =
                                sharedTransitionScope.rememberSharedContentState(
                                    key = "title-${state.note.noteId}"
                                ),
                                animatedVisibilityScope = animatedContentScope
                            )
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .testTag(TestTags.NOTE_TITLE_INPUT),
                        value = state.note.noteTitle ?: "",
                        onValueChange = onChangeTitle,
                        textStyle = MaterialTheme.typography.titleMedium,
                        label = R.string.note_title,
                        minLines = 1
                    )
                }
                item {
                    TogaTextField(
                        modifier =
                        Modifier
                            .sharedElement(
                                state =
                                sharedTransitionScope.rememberSharedContentState(
                                    key = "text-${state.note.noteId}"
                                ),
                                animatedVisibilityScope = animatedContentScope
                            )
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .testTag(TestTags.NOTE_TEXT_INPUT),
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
                        Checkbox(
                            checked = state.note.isCheckList,
                            onCheckedChange = onToggleCheckList,
                            modifier = Modifier.testTag(TestTags.TOGGLE_CHECKLIST_BUTTON)
                        )
                        TogaLargeLabel(R.string.has_checklist)
                    }
                }
                item {
                    AnimatedVisibility(visible = state.note.isCheckList) {
                        CheckList(
                            modifier =
                                Modifier.sharedElement(
                                        state =
                                            sharedTransitionScope.rememberSharedContentState(
                                                key = "checklist-${state.note.noteId}"
                                            ),
                                        animatedVisibilityScope = animatedContentScope
                                    )
                                    .testTag(TestTags.CHECK_LIST),
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
                        Checkbox(
                            checked = state.hasTags,
                            onCheckedChange = onToggleTags,
                            modifier = Modifier.testTag(TestTags.NOTE_TAG_TOGGLE)
                        )
                        TogaLargeLabel(R.string.has_tags, modifier = Modifier.weight(1f))
                        AnimatedVisibility(visible = state.hasTags, modifier = Modifier) {
                            TogaIconButton(
                                modifier = Modifier.testTag(TestTags.ADD_TAG_BUTTON),
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
                            modifier = Modifier.testTag(TestTags.TAG_LIST),
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
                                modifier = Modifier.testTag(TestTags.REMINDER_ROW),
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
}

// Helper function to check and request SCHEDULE_EXACT_ALARM permission
fun checkAndRequestAlarmPermission(alarmManager: AlarmManager, context: Context) {}

// Function to open settings for SCHEDULE_EXACT_ALARM permission
fun requestAlarmPermission(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        // Launch intent to request alarm permission
        val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
        context.startActivity(intent)
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

internal object TestTags {
    const val NOTE_TITLE_INPUT = "note_title_input"
    const val NOTE_TEXT_INPUT = "note_text_input"
    const val SAVE_BUTTON = "save_button"
    const val NOTE_COLOR_PICKER = "note_color_picker"
    const val NOTE_TAG_TOGGLE = "note_tag_toggle"
    const val TOGGLE_PIN_BUTTON = "toggle_pin_button"
    const val TOGGLE_REMINDER_BUTTON = "toggle_reminder_button"
    const val REMINDER_BOTTOM_SHEET = "reminder_bottom_sheet"
    const val DATE_DIALOG = "date_dialog"
    const val TIME_DIALOG = "time_dialog"
    const val REMINDER_ROW = "reminder_row"
    const val TAG_BOTTOM_SHEET = "tag_bottom_sheet"
    const val TOGGLE_CHECKLIST_BUTTON = "toggle_checklist_button"
    const val CHECK_LIST = "check_list"
    const val TAG_LIST = "tag_list"
    const val TOGGLE_COLOR_BUTTON = "toggle_color_button"
    const val ADD_TAG_BUTTON = "add_tag_button"
    const val ADD_NEW_CHECKLIST_ITEM_BUTTON = "add_new_checklist_item_button"
    const val NEW_CHECKLIST_ITEM_TEXT_FIELD = "new_checklist_item_text_field"
    const val REMINDER_DATE = "reminder_date"
    const val REMINDER_TIME = "reminder_time"
    const val REMINDER_TIME_ENTRY = "reminder_time_entry"
    const val SECURE_BUTTON = "secure_button"
    const val PASSWORD_SHEET = "password_sheet"
}
