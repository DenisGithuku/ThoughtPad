
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.gitsoft.thoughtpad.core.toga.R
import com.gitsoft.thoughtpad.core.toga.components.button.TogaPrimaryButton
import com.gitsoft.thoughtpad.core.toga.components.button.TogaTextButton
import com.gitsoft.thoughtpad.core.toga.components.text.TogaButtonText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TogaAlertDialog(onConfirm: () -> Unit, onDismissRequest: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TogaPrimaryButton(onClick = onConfirm) { TogaButtonText(text = stringResource(R.string.ok)) }
        },
        dismissButton = { TogaTextButton(text = R.string.cancel, onClick = onDismissRequest) }
    )
}
