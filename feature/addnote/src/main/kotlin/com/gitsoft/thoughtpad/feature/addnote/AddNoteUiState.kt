
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

import com.gitsoft.thoughtpad.core.model.CheckListItem
import com.gitsoft.thoughtpad.core.model.Note
import com.gitsoft.thoughtpad.core.model.NoteColor
import com.gitsoft.thoughtpad.core.model.Tag
import com.gitsoft.thoughtpad.core.model.TagColor
import kotlin.random.Random

data class AddNoteUiState(
    val note: Note = Note(),
    val isColorVisible: Boolean = false,
    val hasReminder: Boolean = false,
    val timeDialogIsVisible: Boolean = false,
    val dateDialogIsVisible: Boolean = false,
    val isDateSheetVisible: Boolean = false,
    val selectedDate: Long? = null,
    val hasTags: Boolean = false,
    val systemInDarkMode: Boolean = false,
    val noteColors: List<NoteColor> =
        listOf(
            NoteColor.Default,
            NoteColor.Blue,
            NoteColor.Green,
            NoteColor.Cyan,
            NoteColor.Yellow,
            NoteColor.Lavender,
            NoteColor.BurntOrange,
            NoteColor.Pink,
            NoteColor.Coral,
            NoteColor.SoftGreen
        ),
    val tagColors: List<TagColor> =
        listOf(
            TagColor.Blue,
            TagColor.Green,
            TagColor.Teal,
            TagColor.Orange,
            TagColor.Purple,
            TagColor.Red,
            TagColor.Yellow,
            TagColor.Brown
        ),
    val selectedNoteColor: NoteColor = noteColors.first(),
    val selectedTagColor: TagColor = tagColors[Random(8).nextInt(tagColors.size)],
    val checkListItems: List<CheckListItem> = emptyList(),
    val selectedTags: List<Tag> = emptyList(),
    val messages: List<String> = emptyList(),
    val defaultTags: List<Tag> = emptyList(),
    val isTagSheetVisible: Boolean = false,
    val insertionSuccessful: Boolean = false,
    val isNewNote: Boolean = true
) {
    val noteIsValid: Boolean
        get() {
            return note.noteTitle?.isNotBlank() == true && note.noteText?.isNotBlank() == true
        }
}
