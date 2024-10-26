
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

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gitsoft.thoughtpad.core.model.Tag
import com.gitsoft.thoughtpad.core.model.TagColor
import com.gitsoft.thoughtpad.feature.addnote.R
import com.gitsoft.thoughtpad.feature.addnote.TagColorPicker
import core.gitsoft.thoughtpad.core.toga.components.button.TogaIconButton
import core.gitsoft.thoughtpad.core.toga.components.button.TogaPrimaryButton
import core.gitsoft.thoughtpad.core.toga.components.chips.TogaInputChip
import core.gitsoft.thoughtpad.core.toga.components.input.TogaTextField
import core.gitsoft.thoughtpad.core.toga.components.text.TogaButtonText
import core.gitsoft.thoughtpad.core.toga.components.text.TogaDefaultText

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagSelectionContent(
    isSystemInDarkTheme: Boolean,
    existingTags: List<Tag>,
    selectedTags: List<Tag>,
    selectedTagColor: TagColor,
    tagColors: List<TagColor>,
    onToggleTagSelection: (Tag) -> Unit,
    onNewTagAdded: (Tag) -> Unit,
    onChangeTagColor: (TagColor) -> Unit,
    onDismissRequest: () -> Unit
) {
    var tagNameState by rememberSaveable { mutableStateOf("") }
    val context: Context = LocalContext.current

    val tagIsValid =
        remember(tagNameState) {
            derivedStateOf {
                tagNameState.trim().isNotBlank() && existingTags.none { it.name.equals(tagNameState, true) }
            }
        }

    Column(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.weight(1f))
            TogaDefaultText("Select Tags", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.weight(1f))
            TogaIconButton(
                icon = R.drawable.ic_close,
                onClick = onDismissRequest,
                contentDescription = R.string.close
            )
        }

        // Show default tags
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            existingTags.forEach { tag ->
                TogaInputChip(
                    modifier = Modifier.height(48.dp),
                    text = tag.name ?: "",
                    isSelected = selectedTags.any { it isTheSameAs tag },
                    onSelectChanged = { onToggleTagSelection(tag) },
                    color = if (isSystemInDarkTheme) tag.color.darkColor else tag.color.lightColor
                )
            }
        }

        // New tag input
        TogaTextField(
            value = tagNameState,
            onValueChange = { input -> tagNameState = input },
            label = R.string.add_new_tag,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // Tag colors
        AnimatedVisibility(
            visible = tagIsValid.value,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            TagColorPicker(
                isDarkTheme = isSystemInDarkTheme,
                selectedColor = selectedTagColor,
                colors = tagColors,
                onColorSelected = onChangeTagColor
            )
        }

        // Add button
        TogaPrimaryButton(
            enabled = tagIsValid.value,
            onClick = {
                val newTag = Tag(name = tagNameState, color = selectedTagColor)
                onNewTagAdded(newTag)
                onChangeTagColor(tagColors.first())
                tagNameState = ""
                Toast.makeText(context, context.getString(R.string.new_tag_added), Toast.LENGTH_SHORT)
                    .show()
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            TogaButtonText(text = stringResource(id = R.string.add_tag))
        }
    }
}

// Helper function for checking if two tags are the same
infix fun Tag.isTheSameAs(other: Tag): Boolean = this.name.equals(other.name, true)
