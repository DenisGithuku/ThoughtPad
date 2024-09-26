package com.gitsoft.thoughtpad.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Define light and dark color schemes
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0A4DE0), // blue_500
    onPrimary = Color.White, // colorOnPrimary
    primaryContainer = Color(0xFFD6E4FF), // Optional: Primary container color
    onPrimaryContainer = Color(0xFF003DA5), // Optional: On primary container
    secondary = Color(0xFF03DAC5), // teal_200
    onSecondary = Color.Black // colorOnSecondary
    // Define other colors as needed
)

private val DarkColorScheme = darkColorScheme(
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

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}