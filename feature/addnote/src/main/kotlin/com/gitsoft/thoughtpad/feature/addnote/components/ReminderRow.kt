
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gitsoft.thoughtpad.core.toga.components.text.TogaDefaultText
import com.gitsoft.thoughtpad.core.toga.components.text.TogaMediumLabel
import com.gitsoft.thoughtpad.core.toga.components.text.TogaSmallLabel
import com.gitsoft.thoughtpad.feature.addnote.R
import com.gitsoft.thoughtpad.feature.addnote.TestTags
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
    Column(modifier = modifier.fillMaxWidth()) {
        TogaMediumLabel(text = stringResource(id = R.string.reminder_me_at))
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ReminderItem(
                modifier = Modifier.testTag(TestTags.REMINDER_DATE),
                value = reminderTime.toFormattedDate(),
                onClick = { onChangeDate(true) }
            )
            TogaSmallLabel(text = stringResource(id = R.string.at))
            ReminderItem(
                modifier = Modifier.testTag(TestTags.REMINDER_TIME),
                value = reminderTime.toFormattedTime(),
                onClick = { onChangeTime(true) }
            )
        }
    }
}

@Composable
fun ReminderItem(modifier: Modifier = Modifier, value: String, onClick: () -> Unit) {
    Box(
        modifier =
            modifier
                .clip(MaterialTheme.shapes.small)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable { onClick() }
    ) {
        TogaDefaultText(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            text = value,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

fun Long.toFormattedDate(): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this

    val today = Calendar.getInstance() // Current date and time
    val yesterday = Calendar.getInstance().apply { add(Calendar.DATE, -1) } // Yesterday's date
    val tomorrow = Calendar.getInstance().apply { add(Calendar.DATE, 1) } // Tomorrow's date

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
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
        cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}

fun Long.toFormattedTime(): String {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(this) // 'this' is the Long value in milliseconds
}
