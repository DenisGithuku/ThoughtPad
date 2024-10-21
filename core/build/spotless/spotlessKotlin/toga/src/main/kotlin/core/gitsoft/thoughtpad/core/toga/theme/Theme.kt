
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

import android.app.Activity
import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Light Theme Color Scheme
val LightColorScheme =
    lightColorScheme(
        primary = SavvyBlue,
        onPrimary = CrystalWhite,
        primaryContainer = SavvyBlueLight,
        onPrimaryContainer = SavvyBlueDark,
        secondary = OceanWave,
        onSecondary = CrystalWhite,
        secondaryContainer = OceanWaveLight,
        onSecondaryContainer = OceanWaveDark,
        background = SnowDrift,
        onBackground = MidnightInk,
        surface = CrystalWhite,
        onSurface = SmokyGray
    )

// Dark Theme Color Scheme
val DarkColorScheme =
    darkColorScheme(
        primary = SavvyBlueDarkMode,
        onPrimary = SavvyBlueOnDark,
        primaryContainer = SavvyBlueContainerDark,
        onPrimaryContainer = SavvyBlueLight,
        secondary = OceanWaveDarkMode,
        onSecondary = OceanWaveOnDark,
        secondaryContainer = OceanWaveContainerDark,
        onSecondaryContainer = OceanWaveLight,
        background = DarkBackground,
        onBackground = LightMist,
        surface = CharcoalSurface,
        onSurface = PureWhite
    )

@Composable
fun ThoughtPadTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme =
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val context = LocalContext.current
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            }
            darkTheme -> DarkColorScheme
            else -> LightColorScheme
        }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(colorScheme = colorScheme, content = content, typography = ThoughtPadTypography)
}
