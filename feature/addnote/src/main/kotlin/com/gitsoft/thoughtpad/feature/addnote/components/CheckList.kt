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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.gitsoft.thoughtpad.core.model.CheckListItem
import com.gitsoft.thoughtpad.feature.addnote.R
import core.gitsoft.thoughtpad.core.toga.components.button.TogaIconButton
import core.gitsoft.thoughtpad.core.toga.components.input.TogaTextField
import core.gitsoft.thoughtpad.core.toga.components.text.TogaMediumLabel

@Composable
fun CheckList(
    checklistItems: List<CheckListItem>,
    onCheckedChange: (CheckListItem, Boolean) -> Unit, // Handles check/uncheck
    onAddItem: (CheckListItem) -> Unit, // Handles adding a new checklist item
    onDeleteItem: (CheckListItem) -> Unit // Handles item deletion
) {
    Column {
        // Display existing checklist items
        checklistItems.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = item.isChecked,
                    onCheckedChange = { checked -> onCheckedChange(item, checked) })
                Spacer(modifier = Modifier.width(8.dp))
                TogaMediumLabel(
                    text = item.text ?: "",
                    modifier = Modifier.weight(1f) // Ensures the text takes the remaining space
                )
                TogaIconButton(icon = R.drawable.ic_delete,
                    contentDescription = R.string.delete,
                    onClick = { onDeleteItem(item) })
            }

        }

        // TextField to add new checklist items
        var newItemText by remember { mutableStateOf("") }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TogaTextField(
                value = newItemText,
                onValueChange = { newItemText = it },
                label = R.string.add_new_item,
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences, imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    if (newItemText.isNotEmpty()) {
                        onAddItem(CheckListItem(text = newItemText))
                        newItemText = ""
                    }
                }) // Takes up the rest of the row
            )
            TogaIconButton(
                onClick = {
                    if (newItemText.isNotEmpty()) {
                        onAddItem(CheckListItem(text = newItemText))
                        newItemText = ""
                    }
                }, icon = R.drawable.ic_add, contentDescription = R.string.add_new_item
            )
        }
    }
}