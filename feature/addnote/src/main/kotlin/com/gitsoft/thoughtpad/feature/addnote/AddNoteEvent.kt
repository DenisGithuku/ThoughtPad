
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
import com.gitsoft.thoughtpad.core.model.NoteColor
import com.gitsoft.thoughtpad.core.model.Tag
import com.gitsoft.thoughtpad.core.model.TagColor

sealed interface AddNoteEvent {
    data class ChangeTitle(val value: String) : AddNoteEvent

    data class ChangeText(val value: String) : AddNoteEvent

    data class AddCheckListItem(val checkListItem: CheckListItem) : AddNoteEvent

    data class ToggleTagSelection(val tag: Tag) : AddNoteEvent

    data class AddTag(val tag: Tag) : AddNoteEvent

    data class RemoveTag(val tag: Tag) : AddNoteEvent

    data class ToggleTags(val value: Boolean) : AddNoteEvent

    data class RemoveCheckListItem(val checkListItem: CheckListItem) : AddNoteEvent

    data class ToggleCheckList(val value: Boolean) : AddNoteEvent

    data class ChangeNoteColor(val value: NoteColor) : AddNoteEvent

    data class ChangeTagColor(val value: TagColor) : AddNoteEvent

    data class ToggleColorBar(val value: Boolean) : AddNoteEvent

    data class TogglePin(val value: Boolean) : AddNoteEvent

    data class ToggleReminders(val value: Boolean) : AddNoteEvent

    data class ToggleDateSheet(val value: Boolean) : AddNoteEvent

    data class CheckListItemCheckedChange(val checkListItem: CheckListItem, val checked: Boolean) :
        AddNoteEvent

    data class ToggleTagSheet(val isVisible: Boolean) : AddNoteEvent

    data class ToggleDateDialog(val value: Boolean) : AddNoteEvent

    data class ToggleTimeDialog(val value: Boolean) : AddNoteEvent

    data class ChangeDate(val value: Long) : AddNoteEvent

    data class ChangeTime(val value: Long) : AddNoteEvent

    data object DiscardNote : AddNoteEvent

    data object UpdateNotificationPermissions : AddNoteEvent

    data object Save : AddNoteEvent
}
