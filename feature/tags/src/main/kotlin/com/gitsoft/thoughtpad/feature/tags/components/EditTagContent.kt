
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

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gitsoft.thoughtpad.core.model.Tag
import com.gitsoft.thoughtpad.core.model.TagColor
import com.gitsoft.thoughtpad.feature.tags.R
import core.gitsoft.thoughtpad.core.toga.components.button.TogaPrimaryButton
import core.gitsoft.thoughtpad.core.toga.components.input.TogaTextField
import core.gitsoft.thoughtpad.core.toga.components.text.TogaButtonText

@Composable
fun EditTagContent(
    isSystemInDarkTheme: Boolean,
    selectedTag: Tag,
    tagColors: List<TagColor>,
    onEditTagColor: (TagColor) -> Unit,
    onEditTagName: (String) -> Unit,
    onTagUpdated: () -> Unit
) {
    val context = LocalContext.current

    val initialData by remember { mutableStateOf(Pair(selectedTag.name, selectedTag.color)) }

    Column(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // New tag input
        TogaTextField(
            value = selectedTag.name ?: "",
            onValueChange = { onEditTagName(it) },
            label = R.string.add_new_tag,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // Tag colors
        AnimatedVisibility(
            visible = selectedTag.name?.trim()?.isNotEmpty() == true,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            TagColorPicker(
                isDarkTheme = isSystemInDarkTheme,
                selectedColor = selectedTag.color,
                colors = tagColors,
                onColorSelected = onEditTagColor
            )
        }
        // Add button
        TogaPrimaryButton(
            enabled =
                selectedTag.name?.trim()?.isNotEmpty() == true ||
                    initialData.first.equals(selectedTag.name, true) ||
                    initialData.second != selectedTag.color,
            onClick = {
                onTagUpdated()
                Toast.makeText(context, context.getString(R.string.tag_updated), Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            TogaButtonText(text = stringResource(id = R.string.update))
        }
    }
}
