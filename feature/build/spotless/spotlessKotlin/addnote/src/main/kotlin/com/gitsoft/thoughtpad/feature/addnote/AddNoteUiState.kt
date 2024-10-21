
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

import androidx.compose.ui.graphics.Color
import com.gitsoft.thoughtpad.core.model.CheckListItem
import com.gitsoft.thoughtpad.core.model.Note
import com.gitsoft.thoughtpad.core.model.Tag
import core.gitsoft.thoughtpad.core.toga.theme.BlushPink
import core.gitsoft.thoughtpad.core.toga.theme.LightCyan
import core.gitsoft.thoughtpad.core.toga.theme.LightLavender
import core.gitsoft.thoughtpad.core.toga.theme.LightYellow
import core.gitsoft.thoughtpad.core.toga.theme.MintGreen
import core.gitsoft.thoughtpad.core.toga.theme.PaleGreen
import core.gitsoft.thoughtpad.core.toga.theme.Peach
import core.gitsoft.thoughtpad.core.toga.theme.SnowDrift
import core.gitsoft.thoughtpad.core.toga.theme.SoftBlue
import core.gitsoft.thoughtpad.core.toga.theme.SoftCoral

data class AddNoteUiState(
    val note: Note = Note(),
    val isColorVisible: Boolean = false,
    val isReminderDialogVisible: Boolean = false,
    val hasTags: Boolean = false,
    val colors: List<Color> =
        listOf(
            SnowDrift,
            SoftBlue,
            PaleGreen,
            Peach,
            LightLavender,
            LightYellow,
            MintGreen,
            SoftCoral,
            LightCyan,
            BlushPink
        ),
    val selectedColor: Color = colors.first(),
    val checkListItems: List<CheckListItem> = emptyList(),
    val tags: List<Tag> = emptyList(),
    val messages: List<String> = emptyList()
) {
    val noteIsValid: Boolean
        get() {
            return note.noteTitle?.isNotBlank() == true && note.noteText?.isNotBlank() == true
        }
}
