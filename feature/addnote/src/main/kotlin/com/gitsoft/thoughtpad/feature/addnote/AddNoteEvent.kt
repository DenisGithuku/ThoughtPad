
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

sealed interface AddNoteEvent {
    data class ChangeNoteTitle(val value: String) : AddNoteEvent

    data class ChangeNoteText(val value: String) : AddNoteEvent

    data class AddNoteCheckList(val value: String) : AddNoteEvent

    data class AddNoteTag(val value: String) : AddNoteEvent

    data class DeductNoteTag(val value: String) : AddNoteEvent

    data object ToggleNoteTags : AddNoteEvent

    data class DeductNoteCheckList(val value: String) : AddNoteEvent

    data object ToggleNoteCheckList : AddNoteEvent

    data class ChangeNoteColor(val value: String) : AddNoteEvent

    data object Save : AddNoteEvent
}
