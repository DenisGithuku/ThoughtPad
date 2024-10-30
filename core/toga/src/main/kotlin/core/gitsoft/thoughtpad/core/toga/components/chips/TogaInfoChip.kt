
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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import core.gitsoft.thoughtpad.core.toga.components.text.TogaMediumLabel
import core.gitsoft.thoughtpad.core.toga.theme.toComposeColor

@Composable
fun TogaInfoChip(
    modifier: Modifier = Modifier,
    text: String,
    color: Long? = null,
    textStyle: TextStyle = MaterialTheme.typography.labelMedium
) {
    Box(
        modifier =
            modifier
                .clip(MaterialTheme.shapes.extraLarge)
                .background(color = color?.toComposeColor() ?: MaterialTheme.colorScheme.onSurface)
    ) {
        TogaMediumLabel(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            text = text,
            style = textStyle
        )
    }
}
