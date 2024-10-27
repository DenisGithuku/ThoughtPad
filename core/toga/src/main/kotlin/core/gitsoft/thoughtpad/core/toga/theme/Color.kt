
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
package core.gitsoft.thoughtpad.core.toga.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

// Light App Theme
val SavvyBlue = Color(0xFF0A4DE0)
val SavvyBlueLight = Color(0xFFDCE3FF)
val SavvyBlueDark = Color(0xFF001B6A)
val OceanWave = Color(0xFF005AC7)
val OceanWaveLight = Color(0xFFCCE0FF)
val OceanWaveDark = Color(0xFF002D73)
val SnowDrift = Color(0xFFF5F5F5)
val MidnightInk = Color(0xFF000000)
val CrystalWhite = Color(0xFFFFFFFF)
val SmokyGray = Color(0xFF1A1A1A)

// Dark App Theme
val SavvyBlueDarkMode = Color(0xFF5989FF)
val SavvyBlueOnDark = Color(0xFF00248C)
val SavvyBlueContainerDark = Color(0xFF002D6C)
val OceanWaveDarkMode = Color(0xFF8CCBFF)
val GentleBreeze = Color(0xFF84C9FB)
val OceanWaveOnDark = Color(0xFF00366A)
val OceanWaveContainerDark = Color(0xFF004B9D)
val DarkBackground = Color(0xFF1D262F)
val LightMist = Color(0xFFE0E0E0)
val CharcoalSurface = Color(0xFF232D39)
val PureWhite = Color(0xFFFFFFFF)

// Light Theme Tag Colors
val TagRed = Color(0xFFFF6B6B)
val TagOrange = Color(0xFFFFA726)
val TagYellow = Color(0xFFFFEB3B)
val TagGreen = Color(0xFF66BB6A)
val TagTeal = Color(0xFF26A69A)
val TagBlue = Color(0xFF42A5F5)
val TagPurple = Color(0xFFAB47BC)
val TagBrown = Color(0xFF8D6E63)

// Dark Theme Tag Colors
val TagRedDark = Color(0xFFEF5350)
val TagOrangeDark = Color(0xFFFF8A65)
val TagYellowDark = Color(0xFFFDD835)
val TagGreenDark = Color(0xFF66BB6A)
val TagTealDark = Color(0xFF26C6DA)
val TagBlueDark = Color(0xFF1E88E5)
val TagPurpleDark = Color(0xFF8E24AA)
val TagBrownDark = Color(0xFF6D4C41)

// Light Note Background Colors
val LightGray = Color(0xFFF5F5F5) // Light Gray
val SoftBlue = Color(0xFF84C9FB) // Soft Blue
val PaleGreen = Color(0xFFD9EAD3) // Pale Green
val LightBurntOrange = Color(0xFFFFB84D) // Light Burnt Orange
val LightLavender = Color(0xFFEAD1F7) // Light Lavender
val LightYellow = Color(0xFFFFF9B1) // Light Yellow
val MintGreen = Color(0xFFB2F2B2) // Mint Green
val SoftCoral = Color(0xFFFF6F61) // Soft Coral
val LightCyan = Color(0xFFE0F7FA) // Light Cyan
val BlushPink = Color(0xFFF8D2D0) // Blush Pink

// Dark Note Background Colors
val DarkGray = Color(0xFF1A1A1A) // Dark Gray
val DeepBlue = Color(0xFF1F2C3D) // Deep Blue
val OliveGreen = Color(0xFF4A5E1B) // Olive Green
val DarkBurntOrange = Color(0xFF992200) // Burnt Orange
val DeepLavender = Color(0xFF5A4C8C) // Deep Lavender
val MustardYellow = Color(0xFF8A8C25) // Mustard Yellow
val ForestGreen = Color(0xFF1B4A1B) // Forest Green
val DarkCoral = Color(0xFF9A4C3A) // Dark Coral
val TealCyan = Color(0xFF006B6B) // Teal Cyan
val DuskyPink = Color(0xFF7E1E1E) // Dusky Pink

val Success = Color(0xFF4CAF50) // Green for success
val Error = Color(0xFFF44336) // Red for error
val Info = Color(0xFF2196F3) // Blue for info

fun Color.toComposeLong(): Long = this.toArgb().toLong()

fun Long.toComposeColor(): Color = Color(this.toULong() shl 32)
