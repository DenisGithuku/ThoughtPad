
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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.gitsoft.thoughtpad.core.toga.R
import com.gitsoft.thoughtpad.core.toga.components.button.TogaPrimaryButton
import com.gitsoft.thoughtpad.core.toga.components.button.TogaTextButton
import com.gitsoft.thoughtpad.core.toga.components.input.TogaTextField
import com.gitsoft.thoughtpad.core.toga.components.text.TogaButtonText
import com.gitsoft.thoughtpad.core.toga.components.text.TogaCaption

@Composable
fun SecureNoteContent(
    password: String,
    onPasswordChange: (String) -> Unit,
    onSecureNote: () -> Unit,
    onDismissRequest: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().imePadding().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TogaTextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            supportingText = {
                TogaCaption(
                    modifier = Modifier,
                    caption = stringResource(id = R.string.password_size_requirement),
                    color = MaterialTheme.colorScheme.error
                )
            },
            label = R.string.password_label,
            onValueChange = onPasswordChange,
            keyboardOptions =
                KeyboardOptions(imeAction = if (password.length >= 4) ImeAction.Done else ImeAction.Default)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TogaTextButton(text = R.string.cancel, onClick = onDismissRequest)
            TogaPrimaryButton(
                modifier = Modifier.padding(start = 8.dp),
                onClick = onSecureNote,
                enabled = password.length >= 4
            ) {
                TogaButtonText(text = stringResource(R.string.secure_note))
            }
        }
    }
}
