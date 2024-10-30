
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
package com.gitsoft.thoughtpad.feature.tags.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gitsoft.thoughtpad.core.model.TagColor
import core.gitsoft.thoughtpad.core.toga.theme.toComposeColor

@Composable
fun TagColorPicker(
    isDarkTheme: Boolean,
    selectedColor: TagColor,
    colors: List<TagColor>,
    onColorSelected: (TagColor) -> Unit
) {
    LazyRow(
        modifier = Modifier.padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = colors, key = { it.ordinal }) { colorValue ->
            ColorPill(
                color =
                    if (isDarkTheme) {
                        colorValue.darkColor.toComposeColor()
                    } else colorValue.lightColor.toComposeColor(),
                isSelected = colorValue == selectedColor,
                onSelect = {
                    onColorSelected(
                        TagColor.entries.find { tagColor ->
                            tagColor.darkColor.toComposeColor() == it ||
                                tagColor.lightColor.toComposeColor() == it
                        } ?: TagColor.Blue
                    )
                }
            )
        }
    }
}
