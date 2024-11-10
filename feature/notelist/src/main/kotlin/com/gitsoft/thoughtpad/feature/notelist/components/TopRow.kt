
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
package com.gitsoft.thoughtpad.feature.notelist.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.gitsoft.thoughtpad.core.model.NoteListType
import com.gitsoft.thoughtpad.core.toga.components.button.TogaIconButton
import com.gitsoft.thoughtpad.core.toga.components.input.TogaSearchBar
import com.gitsoft.thoughtpad.feature.notelist.R
import com.gitsoft.thoughtpad.feature.notelist.TestTags

@Composable
fun TopRow(
    query: String,
    selectedNoteListType: NoteListType,
    onToggleSideBar: () -> Unit,
    onQueryChange: (String) -> Unit,
    onToggleNoteList: (NoteListType) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(PaddingValues(vertical = 16.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TogaIconButton(
            modifier = Modifier.sizeIn(24.dp).testTag(TestTags.SIDEBAR_TOGGLE),
            icon = R.drawable.ic_side_menu,
            contentDescription = R.string.sidebar_toggle,
            onClick = onToggleSideBar
        )
        TogaSearchBar(
            modifier = Modifier.weight(1f).testTag(TestTags.SEARCH_BAR),
            query = query,
            onQueryChange = onQueryChange,
            onSearch = {}
        )
        TogaIconButton(
            modifier = Modifier.sizeIn(24.dp).testTag(TestTags.NOTE_LIST_TYPE),
            icon =
                when (selectedNoteListType) {
                    NoteListType.GRID -> R.drawable.ic_note_list
                    NoteListType.LIST -> R.drawable.ic_grid
                },
            contentDescription =
                when (selectedNoteListType) {
                    NoteListType.GRID -> R.string.note_list
                    NoteListType.LIST -> R.string.note_grid
                },
            onClick = {
                onToggleNoteList(
                    when (selectedNoteListType) {
                        NoteListType.GRID -> NoteListType.LIST
                        NoteListType.LIST -> NoteListType.GRID
                    }
                )
            }
        )
    }
}
