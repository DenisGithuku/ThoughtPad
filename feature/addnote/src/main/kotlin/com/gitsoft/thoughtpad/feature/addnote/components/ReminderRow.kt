package com.gitsoft.thoughtpad.feature.addnote.components

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gitsoft.thoughtpad.feature.addnote.R
import core.gitsoft.thoughtpad.core.toga.components.text.TogaDefaultText
import core.gitsoft.thoughtpad.core.toga.components.text.TogaMediumLabel
import core.gitsoft.thoughtpad.core.toga.components.text.TogaSmallLabel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun ReminderRow(
    modifier: Modifier = Modifier,
    reminderTime: Long,
    onChangeDate: (Boolean) -> Unit,
    onChangeTime: (Boolean) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        TogaMediumLabel(
            text = stringResource(id = R.string.reminder_me_at),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ReminderItem(value = reminderTime.toFormattedDate(), onClick = { onChangeDate(true) })
            TogaSmallLabel(text = stringResource(id = R.string.at))
            ReminderItem(value = reminderTime.toFormattedTime(), onClick = { onChangeTime(true) })
        }
    }
}

@Composable
fun ReminderItem(
    value: String, onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onClick() },
    ) {
        TogaDefaultText(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            text = value,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

fun Long.toFormattedDate(): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this

    val today = Calendar.getInstance()  // Current date and time
    val yesterday = Calendar.getInstance().apply { add(Calendar.DATE, -1) }  // Yesterday's date
    val tomorrow = Calendar.getInstance().apply { add(Calendar.DATE, 1) }  // Tomorrow's date

    return when {
        isSameDay(calendar, yesterday) -> "Yesterday"
        isSameDay(calendar, today) -> "Today"
        isSameDay(calendar, tomorrow) -> "Tomorrow"
        else -> {
            val dateFormat = SimpleDateFormat("MMM dd yyyy", Locale.getDefault())
            dateFormat.format(calendar.time)
        }
    }
}

// Helper function to check if two Calendar instances represent the same day
fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(
        Calendar.DAY_OF_YEAR
    )
}

fun Long.toFormattedTime(): String {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(this)  // 'this' is the Long value in milliseconds
}

