
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
package core.gitsoft.thoughtpad.core.toga.components.input

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.gitsoft.thoughtpad.core.toga.R
import core.gitsoft.thoughtpad.core.toga.components.button.TogaIconButton

@Composable
fun TogaSearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    TextField(
        textStyle = MaterialTheme.typography.labelMedium,
        value = query,
        placeholder = {
            Text(
                text = "Search notes",
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        singleLine = true,
        maxLines = 1,
        shape = CircleShape,
        colors =
            TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
        leadingIcon = {
            TogaIconButton(
                icon = R.drawable.ic_search,
                contentDescription = R.string.search,
                onClick = onSearch
            )
        },
        trailingIcon = {
            AnimatedVisibility(
                enter = scaleIn(initialScale = 0.5f) + fadeIn(initialAlpha = 0.3f),
                exit = scaleOut(targetScale = 0.5f) + fadeOut(targetAlpha = 0.3f),
                visible = query.isNotEmpty()
            ) {
                TogaIconButton(
                    icon = R.drawable.ic_close,
                    contentDescription = R.string.clear,
                    onClick = { onQueryChange("") }
                )
            }
        },
        onValueChange = onQueryChange,
        modifier = modifier.height(56.dp)
    )
}
