
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
package core.gitsoft.thoughtpad.core.toga.components.chips

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gitsoft.thoughtpad.core.toga.R
import core.gitsoft.thoughtpad.core.toga.components.text.TogaMediumLabel
import core.gitsoft.thoughtpad.core.toga.theme.toComposeColor

@Composable
fun TogaInputChip(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
    onSelectChanged: (Boolean) -> Unit,
    color: Long? = null
) {
    Box(
        modifier =
            modifier
                .clip(MaterialTheme.shapes.extraLarge)
                .background(color?.toComposeColor() ?: MaterialTheme.colorScheme.surfaceVariant)
                .clickable(onClick = { onSelectChanged(!isSelected) }),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TogaMediumLabel(text = text)
            AnimatedVisibility(
                visible = isSelected,
                enter = scaleIn(initialScale = 0.5f) + fadeIn(initialAlpha = 0.3f),
                exit = scaleOut(targetScale = 0.5f) + fadeOut(targetAlpha = 0.3f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_check),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun TogaFilterChipPrev() {
    TogaInputChip(text = "Work", isSelected = true, onSelectChanged = {}, color = 3326114816)
}
