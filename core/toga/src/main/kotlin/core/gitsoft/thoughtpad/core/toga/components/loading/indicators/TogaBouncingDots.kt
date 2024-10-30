
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
package core.gitsoft.thoughtpad.core.toga.components.loading.indicators

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TogaBouncingDots(
    modifier: Modifier = Modifier,
    dotSize: Dp = 10.dp,
    animationDelay: Int = 300,
    dotColor: Color = MaterialTheme.colorScheme.primary,
    dotCount: Int = 3 // Allow flexibility for the number of dots
) {
    // Use rememberSaveable to preserve state across configuration changes
    val dots = rememberSaveable { List(dotCount) { Animatable(initialValue = 0f) } }

    // Launch animations in parallel for each dot
    LaunchedEffect(Unit) {
        dots.forEachIndexed { index, animatable ->
            // Launch a delay for each dot
            launch {
                delay((animationDelay / dots.size).toLong() * (index + 1))
                animatable.animateTo(
                    targetValue = 10.dp.value,
                    animationSpec =
                        infiniteRepeatable(
                            animation = tween(durationMillis = animationDelay, easing = FastOutSlowInEasing),
                            repeatMode = RepeatMode.Reverse
                        )
                )
            }
        }
    }

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        dots.mapIndexed { index, animatable ->
            if (index != 0) {
                Spacer(modifier = Modifier.width(4.dp)) // Simplified
            }
            Box(
                modifier =
                    Modifier.sizeIn(dotSize)
                        .offset { IntOffset(x = 0, y = -animatable.value.dp.roundToPx()) }
                        .clip(CircleShape)
                        .background(color = dotColor)
            )
        }
    }
}
