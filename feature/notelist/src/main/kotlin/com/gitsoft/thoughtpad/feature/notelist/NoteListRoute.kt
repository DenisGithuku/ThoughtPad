
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
package com.gitsoft.thoughtpad.feature.notelist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NoteListRoute(onCreateNewNote: () -> Unit, onOpenNoteDetail: (String) -> Unit) {
    NoteListScreen(onCreateNewNote, onOpenNoteDetail)
}

@Composable
internal fun NoteListScreen(onCreateNewNote: () -> Unit, onOpenNoteDetail: (String) -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column {
            Text(text = "Note list")
            Row {
                Button(onClick = { onOpenNoteDetail("") }) { Text(text = "Detail") }
                Spacer(modifier = Modifier.width(10.dp))
                Button(onClick = onCreateNewNote) { Text(text = "New note") }
            }
        }
    }
}
