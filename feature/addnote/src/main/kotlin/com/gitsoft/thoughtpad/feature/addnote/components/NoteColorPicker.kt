
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gitsoft.thoughtpad.core.model.NoteColor
import core.gitsoft.thoughtpad.core.toga.theme.toComposeColor

@Composable
fun NoteColorPicker(
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier,
    selectedColor: NoteColor,
    colors: List<NoteColor>,
    onChangeColor: (NoteColor) -> Unit
) {
    LazyRow(
        modifier = modifier.fillMaxWidth().padding(PaddingValues(horizontal = 16.dp, vertical = 8.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = colors) {
            ColorPill(
                color = if (isDarkTheme) it.darkColor.toComposeColor() else it.lightColor.toComposeColor(),
                isSelected = it == selectedColor,
                onSelect = {
                    onChangeColor(
                        NoteColor.entries.find { noteColor ->
                            noteColor.darkColor.toComposeColor() == it ||
                                noteColor.lightColor.toComposeColor() == it
                        } ?: NoteColor.entries.first()
                    )
                }
            )
        }
    }
}
