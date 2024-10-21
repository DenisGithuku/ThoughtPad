
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
import com.gitsoft.thoughtpad.core.model.Tag

sealed interface AddNoteEvent {
    data class ChangeNoteTitle(val value: String) : AddNoteEvent

    data class ChangeNoteText(val value: String) : AddNoteEvent

    data class AddNoteCheckListItem(val checkListItem: CheckListItem) : AddNoteEvent

    data class AddNoteTag(val tag: Tag) : AddNoteEvent

    data class RemoveTag(val tag: Tag) : AddNoteEvent

    data class ToggleNoteTags(val value: Boolean) : AddNoteEvent

    data class RemoveCheckListItem(val checkListItem: CheckListItem) : AddNoteEvent

    data class ToggleNoteCheckList(val value: Boolean) : AddNoteEvent

    data class ChangeNoteColor(val value: Color) : AddNoteEvent

    data class ToggleColorBar(val value: Boolean) : AddNoteEvent

    data class TogglePin(val value: Boolean) : AddNoteEvent

    data class ToggleReminders(val value: Boolean) : AddNoteEvent

    data class ChangeReminder(val value: Long?) : AddNoteEvent

    data class CheckListItemCheckedChange(val checkListItem: CheckListItem, val checked: Boolean) :
        AddNoteEvent

    data object Save : AddNoteEvent
}
