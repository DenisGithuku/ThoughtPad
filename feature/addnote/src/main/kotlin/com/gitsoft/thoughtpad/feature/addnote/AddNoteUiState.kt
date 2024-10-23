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

import android.icu.util.Calendar
import androidx.compose.ui.graphics.Color
import com.gitsoft.thoughtpad.core.model.CheckListItem
import com.gitsoft.thoughtpad.core.model.Note
import com.gitsoft.thoughtpad.core.model.Tag
import core.gitsoft.thoughtpad.core.toga.theme.BlushPink
import core.gitsoft.thoughtpad.core.toga.theme.BurntOrange
import core.gitsoft.thoughtpad.core.toga.theme.DarkCoral
import core.gitsoft.thoughtpad.core.toga.theme.DarkGray
import core.gitsoft.thoughtpad.core.toga.theme.DeepBlue
import core.gitsoft.thoughtpad.core.toga.theme.DeepLavender
import core.gitsoft.thoughtpad.core.toga.theme.DuskyPink
import core.gitsoft.thoughtpad.core.toga.theme.ForestGreen
import core.gitsoft.thoughtpad.core.toga.theme.LightCyan
import core.gitsoft.thoughtpad.core.toga.theme.LightLavender
import core.gitsoft.thoughtpad.core.toga.theme.LightYellow
import core.gitsoft.thoughtpad.core.toga.theme.MintGreen
import core.gitsoft.thoughtpad.core.toga.theme.MustardYellow
import core.gitsoft.thoughtpad.core.toga.theme.OliveGreen
import core.gitsoft.thoughtpad.core.toga.theme.PaleGreen
import core.gitsoft.thoughtpad.core.toga.theme.Peach
import core.gitsoft.thoughtpad.core.toga.theme.SnowDrift
import core.gitsoft.thoughtpad.core.toga.theme.SoftBlue
import core.gitsoft.thoughtpad.core.toga.theme.SoftCoral
import core.gitsoft.thoughtpad.core.toga.theme.TagBlue
import core.gitsoft.thoughtpad.core.toga.theme.TagBlueDark
import core.gitsoft.thoughtpad.core.toga.theme.TagBrown
import core.gitsoft.thoughtpad.core.toga.theme.TagBrownDark
import core.gitsoft.thoughtpad.core.toga.theme.TagGreen
import core.gitsoft.thoughtpad.core.toga.theme.TagGreenDark
import core.gitsoft.thoughtpad.core.toga.theme.TagOrange
import core.gitsoft.thoughtpad.core.toga.theme.TagOrangeDark
import core.gitsoft.thoughtpad.core.toga.theme.TagPurple
import core.gitsoft.thoughtpad.core.toga.theme.TagPurpleDark
import core.gitsoft.thoughtpad.core.toga.theme.TagRed
import core.gitsoft.thoughtpad.core.toga.theme.TagRedDark
import core.gitsoft.thoughtpad.core.toga.theme.TagTeal
import core.gitsoft.thoughtpad.core.toga.theme.TagTealDark
import core.gitsoft.thoughtpad.core.toga.theme.TagYellow
import core.gitsoft.thoughtpad.core.toga.theme.TagYellowDark
import core.gitsoft.thoughtpad.core.toga.theme.TealCyan

data class AddNoteUiState(
    val note: Note = Note(),
    val isColorVisible: Boolean = false,
    val hasReminder: Boolean = false,
    val timeDialogIsVisible: Boolean = false,
    val dateDialogIsVisible: Boolean = false,
    val selectedDate: Long = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, this.get(Calendar.HOUR_OF_DAY) + 1)
        set(Calendar.MINUTE, 30)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis,
    val hasTags: Boolean = false,
    val systemInDarkMode: Boolean = false,
    val noteColors: List<Color> = if (systemInDarkMode) {
        listOf(
            DarkGray,
            DeepBlue,
            OliveGreen,
            BurntOrange,
            DeepLavender,
            MustardYellow,
            ForestGreen,
            DarkCoral,
            TealCyan,
            DuskyPink
        )
    } else {
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
        )

    },
    val tagColors: List<Color> = if (systemInDarkMode) {
        listOf(
            TagRed,
            TagOrange,
            TagYellow,
            TagGreen,
            TagTeal,
            TagBlue,
            TagPurple,
            TagBrown
        )
    } else {
        listOf(
            TagRedDark,
            TagOrangeDark,
            TagYellowDark,
            TagGreenDark,
            TagTealDark,
            TagBlueDark,
            TagPurpleDark,
            TagBrownDark
        )
    },
    val selectedNoteColor: Color = noteColors.first(),
    val selectedTagColor: Color = tagColors.first(),
    val checkListItems: List<CheckListItem> = emptyList(),
    val selectedTags: List<Tag> = emptyList(),
    val messages: List<String> = emptyList(),
    val defaultTags: List<Tag> = emptyList(),
    val isTagSheetVisible: Boolean = false,
    val insertionSuccessful: Boolean = false
) {
    val noteIsValid: Boolean
        get() {
            return note.noteTitle?.isNotBlank() == true && note.noteText?.isNotBlank() == true
        }
}
