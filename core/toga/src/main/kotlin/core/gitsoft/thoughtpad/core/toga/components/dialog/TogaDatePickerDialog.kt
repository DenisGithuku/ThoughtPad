package core.gitsoft.thoughtpad.core.toga.components.dialog

import androidx.compose.material3.DatePicker
import androidx.compose.runtime.Composable
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.ui.window.DialogProperties
import com.gitsoft.thoughtpad.core.toga.R
import core.gitsoft.thoughtpad.core.toga.components.button.TogaTextButton
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TogaDatePickerDialog(
    onDateSelected: (Long?) -> Unit, onDismiss: () -> Unit
) {

    val yearToday = Calendar.getInstance().get(Calendar.YEAR)

    val datePickerState = rememberDatePickerState(
        initialDisplayMode = DisplayMode.Picker,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= Calendar.getInstance().timeInMillis
            }

            override fun isSelectableYear(year: Int): Boolean {
                return year >= yearToday
            }
        }
        )

    DatePickerDialog(
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
        ),
        onDismissRequest = onDismiss, confirmButton = {
        TogaTextButton(
            onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }, text = R.string.ok
        )
    }, dismissButton = {
        TogaTextButton(
            onClick = onDismiss,
            text = R.string.dimiss,
        )
    }) {
        DatePicker(state = datePickerState)
    }
}