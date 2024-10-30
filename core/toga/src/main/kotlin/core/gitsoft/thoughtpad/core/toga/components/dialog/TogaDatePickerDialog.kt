
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
package core.gitsoft.thoughtpad.core.toga.components.dialog

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties
import com.gitsoft.thoughtpad.core.toga.R
import core.gitsoft.thoughtpad.core.toga.components.button.TogaTextButton
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TogaDatePickerDialog(onDateSelected: (Long?) -> Unit, onDismiss: () -> Unit) {
    val yearToday = Calendar.getInstance().get(Calendar.YEAR)

    val datePickerState =
        rememberDatePickerState(
            initialDisplayMode = DisplayMode.Picker,
            selectableDates =
                object : SelectableDates {
                    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                        return Calendar.getInstance()
                            .apply { timeInMillis = utcTimeMillis }
                            .get(Calendar.DAY_OF_YEAR) >= Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
                    }

                    override fun isSelectableYear(year: Int): Boolean {
                        return year >= yearToday
                    }
                }
        )

    DatePickerDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDismiss,
        confirmButton = {
            TogaTextButton(
                onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
                    onDismiss()
                },
                text = R.string.ok
            )
        },
        dismissButton = { TogaTextButton(onClick = onDismiss, text = R.string.dimiss) }
    ) {
        DatePicker(state = datePickerState)
    }
}
