
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
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun TogaFadingCirclesIndicator(
    modifier: Modifier = Modifier,
    circleSize: Dp = 12.dp,
    animationDelay: Int = 500,
    circleColor: Color = MaterialTheme.colorScheme.primary
) {
    val circles = remember {
        listOf(
            Animatable(initialValue = 1f),
            Animatable(initialValue = 1f),
            Animatable(initialValue = 1f)
        )
    }

    circles.forEachIndexed { index, animatable ->
        /*
        Launch a delay before animating the circle in an Launched effect
         */
        LaunchedEffect(Unit) {
            delay(timeMillis = (animationDelay / circles.size).toLong() * index + 1)
            animatable.animateTo(
                targetValue = 0f,
                animationSpec =
                    infiniteRepeatable(
                        animation = tween(durationMillis = animationDelay, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    )
            )
        }
    }

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        circles.forEachIndexed { index, animatable ->
            if (index != 0) {
                Spacer(modifier = modifier.width(4.dp))
            }
            Box(
                modifier =
                    modifier
                        .size(circleSize)
                        .clip(CircleShape)
                        .background(color = circleColor.copy(alpha = animatable.value))
            )
        }
    }
}
