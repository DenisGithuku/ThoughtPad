
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
package com.gitsoft.thoughtpad.core.toga.components.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.gitsoft.thoughtpad.core.toga.R
import com.gitsoft.thoughtpad.core.toga.components.button.TogaTextButton
import java.util.Calendar

/**
 * A composable function that displays a time picker dialog.
 *
 * @param selectedDate Used to determine whether the date is in the past and therefore disable the
 *   confirm button.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TogaTimePickerDialog(
    modifier: Modifier = Modifier,
    selectedDate: Long,
    onConfirm: (TimePickerState) -> Unit,
    onDismiss: () -> Unit
) {
    val selectedTime = Calendar.getInstance().apply { timeInMillis = selectedDate }

    val currentTime = Calendar.getInstance().apply { timeInMillis = selectedDate }

    val timePickerState =
        rememberTimePickerState(
            initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
            initialMinute = currentTime.get(Calendar.MINUTE),
            is24Hour = true
        )

    var isPastTime by remember { mutableStateOf(false) }

    // Update the isPastTime flag whenever the time changes
    LaunchedEffect(timePickerState.hour, timePickerState.minute) {
        isPastTime =
            isTimeInThePast(timePickerState) &&
                selectedTime.get(Calendar.DAY_OF_YEAR) <= Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
    }

    TimePickerDialog(
        modifier = modifier,
        onDismiss = { onDismiss() },
        onConfirm = { onConfirm(timePickerState) },
        isConfirmEnabled = !isPastTime // Disable the confirm button if the time is in the past
    ) {
        TimePicker(state = timePickerState)
    }
}

@Composable
fun TimePickerDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    isConfirmEnabled: Boolean,
    content: @Composable () -> Unit
) {
    AlertDialog(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        onDismissRequest = onDismiss,
        dismissButton = { TogaTextButton(onClick = onDismiss, text = R.string.dimiss) },
        confirmButton = {
            TogaTextButton(
                onClick = { if (isConfirmEnabled) onConfirm() },
                enabled = isConfirmEnabled,
                text = R.string.ok
            )
        },
        text = { content() }
    )
}

// Helper function to check if the selected time is in the past
@OptIn(ExperimentalMaterial3Api::class)
fun isTimeInThePast(timePickerState: TimePickerState): Boolean {
    val currentTime = Calendar.getInstance()
    val selectedTime =
        Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, timePickerState.hour)
            set(Calendar.MINUTE, timePickerState.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
    return selectedTime.before(currentTime)
}
