
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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.gitsoft.thoughtpad.core.model.CheckListItem
import com.gitsoft.thoughtpad.core.toga.components.button.TogaIconButton
import com.gitsoft.thoughtpad.core.toga.components.input.TogaTextField
import com.gitsoft.thoughtpad.core.toga.components.text.TogaMediumLabel
import com.gitsoft.thoughtpad.feature.addnote.R
import com.gitsoft.thoughtpad.feature.addnote.TestTags

@Composable
fun CheckList(
    modifier: Modifier = Modifier,
    checklistItems: List<CheckListItem>,
    onCheckedChange: (CheckListItem, Boolean) -> Unit, // Handles check/uncheck
    onAddItem: (CheckListItem) -> Unit, // Handles adding a new checklist item
    onDeleteItem: (CheckListItem) -> Unit // Handles item deletion
) {
    Column(modifier = modifier) {
        // Display existing checklist items
        checklistItems.forEach { item ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = item.isChecked,
                    onCheckedChange = { checked -> onCheckedChange(item, checked) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                TogaMediumLabel(
                    text = item.text ?: "",
                    textDecoration = if (item.isChecked) TextDecoration.LineThrough else TextDecoration.None,
                    modifier = Modifier.weight(1f) // Ensures the text takes the remaining space
                )
                TogaIconButton(
                    icon = R.drawable.ic_delete,
                    contentDescription = R.string.delete,
                    onClick = { onDeleteItem(item) }
                )
            }
        }

        // TextField to add new checklist items
        var newItemText by remember { mutableStateOf("") }
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TogaTextField(
                value = newItemText,
                onValueChange = { newItemText = it },
                label = R.string.add_new_item,
                modifier = Modifier.weight(1f).testTag(TestTags.NEW_CHECKLIST_ITEM_TEXT_FIELD),
                singleLine = true,
                keyboardOptions =
                    KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Done
                    ),
                keyboardActions =
                    KeyboardActions(
                        onDone = {
                            if (newItemText.isNotEmpty()) {
                                onAddItem(CheckListItem(text = newItemText))
                                newItemText = ""
                            }
                        }
                    ) // Takes up the rest of the row
            )
            TogaIconButton(
                enabled = newItemText.isNotEmpty(),
                modifier = Modifier.testTag(TestTags.ADD_NEW_CHECKLIST_ITEM_BUTTON),
                onClick = {
                    onAddItem(CheckListItem(text = newItemText))
                    newItemText = ""
                },
                icon = R.drawable.ic_add,
                contentDescription = R.string.add_new_item
            )
        }
    }
}
