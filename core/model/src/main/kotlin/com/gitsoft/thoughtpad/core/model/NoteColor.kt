
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

import core.gitsoft.thoughtpad.core.toga.theme.BlushPink
import core.gitsoft.thoughtpad.core.toga.theme.DarkBackground
import core.gitsoft.thoughtpad.core.toga.theme.DarkBurntOrange
import core.gitsoft.thoughtpad.core.toga.theme.DarkCoral
import core.gitsoft.thoughtpad.core.toga.theme.DeepBlue
import core.gitsoft.thoughtpad.core.toga.theme.DeepLavender
import core.gitsoft.thoughtpad.core.toga.theme.DuskyPink
import core.gitsoft.thoughtpad.core.toga.theme.ForestGreen
import core.gitsoft.thoughtpad.core.toga.theme.LightBurntOrange
import core.gitsoft.thoughtpad.core.toga.theme.LightCyan
import core.gitsoft.thoughtpad.core.toga.theme.LightLavender
import core.gitsoft.thoughtpad.core.toga.theme.LightYellow
import core.gitsoft.thoughtpad.core.toga.theme.MintGreen
import core.gitsoft.thoughtpad.core.toga.theme.MustardYellow
import core.gitsoft.thoughtpad.core.toga.theme.OliveGreen
import core.gitsoft.thoughtpad.core.toga.theme.PaleGreen
import core.gitsoft.thoughtpad.core.toga.theme.SnowDrift
import core.gitsoft.thoughtpad.core.toga.theme.SoftBlue
import core.gitsoft.thoughtpad.core.toga.theme.SoftCoral
import core.gitsoft.thoughtpad.core.toga.theme.TealCyan
import core.gitsoft.thoughtpad.core.toga.theme.toComposeLong

enum class NoteColor(val lightColor: Long, val darkColor: Long) {
    Default(lightColor = SnowDrift.toComposeLong(), darkColor = DarkBackground.toComposeLong()),
    Blue(lightColor = SoftBlue.toComposeLong(), darkColor = DeepBlue.toComposeLong()),
    SoftGreen(lightColor = PaleGreen.toComposeLong(), darkColor = OliveGreen.toComposeLong()),
    Green(lightColor = MintGreen.toComposeLong(), darkColor = ForestGreen.toComposeLong()),
    Pink(lightColor = BlushPink.toComposeLong(), darkColor = DuskyPink.toComposeLong()),
    Cyan(lightColor = LightCyan.toComposeLong(), darkColor = TealCyan.toComposeLong()),
    Coral(lightColor = SoftCoral.toComposeLong(), darkColor = DarkCoral.toComposeLong()),
    Yellow(lightColor = LightYellow.toComposeLong(), darkColor = MustardYellow.toComposeLong()),
    Lavender(lightColor = LightLavender.toComposeLong(), darkColor = DeepLavender.toComposeLong()),
    BurntOrange(
        lightColor = LightBurntOrange.toComposeLong(),
        darkColor = DarkBurntOrange.toComposeLong()
    )
}
