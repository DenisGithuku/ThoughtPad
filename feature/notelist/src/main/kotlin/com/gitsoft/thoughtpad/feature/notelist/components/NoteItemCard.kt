
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
package com.gitsoft.thoughtpad.feature.notelist.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.gitsoft.thoughtpad.core.model.DataWithNotesCheckListItemsAndTags
import com.gitsoft.thoughtpad.core.model.NoteColor
import com.gitsoft.thoughtpad.feature.notelist.R
import core.gitsoft.thoughtpad.core.toga.components.text.TogaSmallBody
import core.gitsoft.thoughtpad.core.toga.components.text.TogaSmallLabel
import core.gitsoft.thoughtpad.core.toga.components.text.TogaSmallTitle
import core.gitsoft.thoughtpad.core.toga.theme.MidnightInk
import core.gitsoft.thoughtpad.core.toga.theme.SnowDrift
import core.gitsoft.thoughtpad.core.toga.theme.toComposeColor
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteItemCard(
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier,
    noteData: DataWithNotesCheckListItemsAndTags,
    isSelected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val borderColor by
        animateColorAsState(
            targetValue =
                when {
                    isSelected -> MaterialTheme.colorScheme.primary
                    noteData.note.color == NoteColor.Default && isDarkTheme -> SnowDrift
                    noteData.note.color == NoteColor.Default && !isDarkTheme -> MidnightInk.copy(alpha = 0.1f)
                    else -> Color.Transparent
                },
            label = "border color"
        )

    val elevation by
        animateDpAsState(targetValue = if (isSelected) 4.dp else 0.dp, label = "elevation")

    val borderWidth by
        animateDpAsState(targetValue = if (isSelected) 3.dp else 1.dp, label = "border width")

    val ambientColor by
        animateColorAsState(
            targetValue =
                if (isSelected) {
                    MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
                } else Color.Transparent,
            label = "ambient color"
        )

    val spotColor by
        animateColorAsState(
            targetValue = if (isSelected) MaterialTheme.colorScheme.scrim else Color.Transparent,
            label = "spot color"
        )

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(shape = MaterialTheme.shapes.medium)
                .shadow(
                    elevation = elevation,
                    shape = MaterialTheme.shapes.medium,
                    ambientColor = ambientColor,
                    spotColor = spotColor
                )
                .border(
                    border = BorderStroke(width = borderWidth, color = borderColor),
                    shape = MaterialTheme.shapes.medium
                )
                .background(
                    color =
                        if (isDarkTheme) {
                            noteData.note.color.darkColor.toComposeColor()
                        } else noteData.note.color.lightColor.toComposeColor(),
                    shape = MaterialTheme.shapes.medium
                )
                .combinedClickable(onClick = onClick, onLongClick = onLongClick)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            // Note title
            noteData.note.noteTitle?.let { title ->
                TogaSmallTitle(modifier = Modifier.fillMaxWidth(), text = title, maxLines = 1)
            }

            // Note text
            noteData.note.noteText?.let { noteText ->
                Spacer(modifier = Modifier.height(4.dp))
                TogaSmallBody(text = noteText, maxLines = 2)
            }

            // Checklist (if available)
            if (noteData.checkListItems.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))

                Column {
                    noteData.checkListItems.take(3).forEach { checklistItem ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 2.dp)
                        ) {
                            Checkbox(
                                checked = checklistItem.isChecked,
                                onCheckedChange = null // Static for display purposes
                            )
                            checklistItem.text?.let {
                                TogaSmallBody(
                                    text = it,
                                    maxLines = 1,
                                    style =
                                        MaterialTheme.typography.bodySmall.copy(
                                            textDecoration =
                                                if (checklistItem.isChecked) {
                                                    TextDecoration.LineThrough
                                                } else {
                                                    TextDecoration.None
                                                }
                                        )
                                )
                            }
                        }
                    }

                    // If there are more than 3 checklist items
                    if (noteData.checkListItems.size > 3) {
                        val text =
                            if (noteData.checkListItems.size - 3 > 1) {
                                "+${noteData.checkListItems.size - 3} more items"
                            } else {
                                "+1 more item"
                            }
                        TogaSmallLabel(text = text)
                    }
                }
            }

            // Additional Info (e.g., pinned or reminder)
            Spacer(modifier = Modifier.height(6.dp))

            noteData.note.reminderTime?.let {
                val formattedReminder = formatReminderDate(it)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(R.drawable.ic_reminder),
                        contentDescription = stringResource(R.string.reminder)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    TogaSmallLabel(text = formattedReminder)
                }
            }
        }
    }
}

fun formatReminderDate(reminderTime: Long): String {
    val currentDate = LocalDate.now()
    val reminderDate = Instant.ofEpochMilli(reminderTime).atZone(ZoneId.systemDefault()).toLocalDate()

    return when {
        reminderDate.isEqual(currentDate) -> formatTime(reminderTime)
        reminderDate.isEqual(currentDate.minusDays(1)) -> "Yesterday"
        reminderDate.isEqual(currentDate.plusDays(1)) -> "Tomorrow"
        else -> reminderDate.format(DateTimeFormatter.ofPattern("MMM dd"))
    }
}

fun formatTime(reminderTime: Long): String {
    val time = Instant.ofEpochMilli(reminderTime).atZone(ZoneId.systemDefault()).toLocalTime()
    return time.format(DateTimeFormatter.ofPattern("hh:mm"))
}
