
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
package com.gitsoft.thoughtpad.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Define light and dark color schemes
private val LightColorScheme =
    lightColorScheme(
        primary = Color(0xFF0A4DE0), // blue_500
        onPrimary = Color.White, // colorOnPrimary
        primaryContainer = Color(0xFFD6E4FF), // Optional: Primary container color
        onPrimaryContainer = Color(0xFF003DA5), // Optional: On primary container
        secondary = Color(0xFF03DAC5), // teal_200
        onSecondary = Color.Black // colorOnSecondary
        // Define other colors as needed
    )

private val DarkColorScheme =
    darkColorScheme(
        primary = Color(0xFF0A4DE0), // blue_500
        onPrimary = Color.White, // colorOnPrimary
        primaryContainer = Color(0xFF003DA5), // Optional: Primary container color
        onPrimaryContainer = Color(0xFFD6E4FF), // Optional: On primary container
        secondary = Color(0xFF018786), // teal_700
        onSecondary = Color.White // colorOnSecondary
        // Define other colors as needed
    )

@Composable
fun ThoughtPadTheme(darkTheme: Boolean = false, content: @Composable () -> Unit) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(colorScheme = colors, content = content)
}
