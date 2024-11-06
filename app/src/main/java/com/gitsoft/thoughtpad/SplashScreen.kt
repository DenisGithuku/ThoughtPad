
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
package com.gitsoft.thoughtpad

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.gitsoft.thoughtpad.core.toga.components.scaffold.TogaBasicScaffold
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onProceed: () -> Unit) {
    val proceed by rememberUpdatedState(onProceed)

    LaunchedEffect(Unit) {
        delay(4000L)
        proceed()
    }
    val text = stringResource(R.string.app_slogan)

    var displayedText by remember { mutableStateOf("") }
    var cursorVisible by remember { mutableStateOf(true) } // For cursor visibility

    LaunchedEffect(text) {
        displayedText = "" // Reset text on new input
        cursorVisible = true // Show cursor
        for (char in text) {
            displayedText += char
            delay(50) // Wait before adding the next character
        }
        cursorVisible = false // Hide cursor after typing
    }

    // Blinking cursor effect
    val infiniteTransition = rememberInfiniteTransition()
    val cursorAnimation by
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec =
                infiniteRepeatable(
                    animation = tween(durationMillis = 500, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
        )

    TogaBasicScaffold { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.quill_drawing_a_line_svgrepo_com),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp),
                    contentScale = ContentScale.Crop,
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary)
                )
                val sloganText = buildAnnotatedString {
                    append(displayedText)
                    if (cursorVisible) {
                        withStyle(
                            style =
                                MaterialTheme.typography.bodyMedium
                                    .copy(color = MaterialTheme.colorScheme.primary.copy(alpha = cursorAnimation))
                                    .toSpanStyle()
                        ) {
                            append("|") // Cursor representation
                        }
                    }
                }
                Text(text = sloganText, textAlign = TextAlign.Center)
            }
        }
    }
}
