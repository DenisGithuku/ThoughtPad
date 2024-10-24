package com.gitsoft.thoughtpad.feature.notelist.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.gitsoft.thoughtpad.core.model.DataWithNotesCheckListItemsAndTags
import com.gitsoft.thoughtpad.feature.notelist.R
import core.gitsoft.thoughtpad.core.toga.components.button.TogaIconButton
import core.gitsoft.thoughtpad.core.toga.components.text.TogaMediumBody
import core.gitsoft.thoughtpad.core.toga.components.text.TogaSmallBody
import core.gitsoft.thoughtpad.core.toga.components.text.TogaSmallLabel
import core.gitsoft.thoughtpad.core.toga.components.text.TogaSmallTitle
import core.gitsoft.thoughtpad.core.toga.theme.TagOrange
import core.gitsoft.thoughtpad.core.toga.theme.toComposeColor
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@Composable
fun NoteItemCard(
    noteData: DataWithNotesCheckListItemsAndTags,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onTogglePin: (Boolean) -> Unit,
    onToggleFavourite: (Boolean) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clip(shape = MaterialTheme.shapes.medium)
            .background(
                color = noteData.note.color.toComposeColor(),
                shape = MaterialTheme.shapes.medium
            )
            .clickable { onClick() },
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Note title
                noteData.note.noteTitle?.let { title ->
                    TogaSmallTitle(
                        modifier = Modifier.weight(1f),
                        text = title,
                        maxLines = 1,
                    )
                }

                TogaIconButton(
                    onClick = { onToggleFavourite(!noteData.note.isFavorite) },
                    icon = if (noteData.note.isFavorite) R.drawable.ic_favourite_filled else R.drawable.ic_favourite,
                    contentDescription = if (noteData.note.isFavorite) R.string.favourite else R.string.unfavourite,
                    tint = if(noteData.note.isFavorite) TagOrange else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Note text
            noteData.note.noteText?.let { noteText ->
                Spacer(modifier = Modifier.height(4.dp))
                TogaMediumBody(
                    text = noteText,
                    maxLines = 2,
                )
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
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        textDecoration = if (checklistItem.isChecked) {
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
                        TogaSmallLabel(
                            text = "+ ${noteData.checkListItems.size - 3} more items",
                        )
                    }
                }
            }

            // Additional Info (e.g., pinned or reminder)
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                val pinColor by animateColorAsState(
                    if (noteData.note.isPinned) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.5f
                    )
                )

                noteData.note.reminderTime?.let {
                    val formattedReminder = formatReminderDate(it)

                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(R.drawable.ic_reminder),
                            contentDescription = stringResource(R.string.reminder),
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        TogaSmallLabel(
                            text = formattedReminder,
                        )
                    }
                }

                val icon = if (noteData.note.isPinned) R.drawable.ic_pin_filled else R.drawable.ic_pin
                TogaIconButton(
                    onClick = { onTogglePin(!noteData.note.isPinned) },
                    icon = icon,
                    contentDescription = if (noteData.note.isPinned) R.string.pin else R.string.unpin,
                    tint = pinColor
                )
            }
        }
    }
}

// Helper function for date formatting (can be adapted to fit your needs)
fun Long.formatAsDate(): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return sdf.format(Date(this))
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