
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
package com.gitsoft.thoughtpad.feature.addnote

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import core.gitsoft.thoughtpad.core.toga.components.TogaStandardScaffold
import core.gitsoft.thoughtpad.core.toga.components.TogaTextButton

@Composable
fun AddNoteRoute(onNavigateBack: () -> Unit) {
    AddNoteScreen(onNavigateBack)
}

@Composable
internal fun AddNoteScreen(onNavigateBack: () -> Unit) {
    TogaStandardScaffold(
        onNavigateBack = onNavigateBack,
        title = R.string.add_note,
        actions = { TogaTextButton(text = R.string.save, onClick = {}) }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Text(text = "Add note")
                Button(onClick = onNavigateBack) { Text(text = "Back") }
            }
        }
    }
}
