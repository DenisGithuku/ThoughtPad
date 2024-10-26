
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
package com.gitsoft.thoughtpad.core.model

import androidx.room.TypeConverter
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
import core.gitsoft.thoughtpad.core.toga.theme.toComposeLong

enum class TagColor(val lightColor: Long, val darkColor: Long) {
    Red(lightColor = TagRed.toComposeLong(), darkColor = TagRedDark.toComposeLong()),
    Orange(lightColor = TagOrange.toComposeLong(), darkColor = TagOrangeDark.toComposeLong()),
    Yellow(lightColor = TagYellow.toComposeLong(), darkColor = TagYellowDark.toComposeLong()),
    Green(lightColor = TagGreen.toComposeLong(), darkColor = TagGreenDark.toComposeLong()),
    Teal(lightColor = TagTeal.toComposeLong(), darkColor = TagTealDark.toComposeLong()),
    Blue(lightColor = TagBlue.toComposeLong(), darkColor = TagBlueDark.toComposeLong()),
    Purple(lightColor = TagPurple.toComposeLong(), darkColor = TagPurpleDark.toComposeLong()),
    Brown(lightColor = TagBrown.toComposeLong(), darkColor = TagBrownDark.toComposeLong())
}

class TagColorConverter {
    @TypeConverter
    fun fromTagColor(tagColor: TagColor): String {
        return tagColor.name
    }

    @TypeConverter
    fun toTagColor(tagColorName: String): TagColor {
        return TagColor.valueOf(tagColorName)
    }
}
