package com.gitsoft.thoughtpad.core.toga.components.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.gitsoft.thoughtpad.core.toga.R
import com.gitsoft.thoughtpad.core.toga.components.button.TogaPrimaryButton
import com.gitsoft.thoughtpad.core.toga.components.button.TogaTextButton
import com.gitsoft.thoughtpad.core.toga.components.text.TogaButtonText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TogaAlertDialog(
    onConfirm: () -> Unit, onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TogaPrimaryButton(onClick = onConfirm) {
                TogaButtonText(
                    text = stringResource(R.string.ok)
                )
            }
        },
        dismissButton = {
            TogaTextButton(
                text = R.string.cancel, onClick = onDismissRequest
            )
        },
    )
}