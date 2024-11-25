
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
package com.gitsoft.thoughtpad.core.toga.components.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape

@Composable
fun TogaPrimaryButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
    shape: Shape = MaterialTheme.shapes.extraLarge,
    border: BorderStroke? = null,
    elevation: ButtonElevation = ButtonDefaults.buttonElevation(),
    colors: ButtonColors =
        ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContentColor = MaterialTheme.colorScheme.onPrimary
        ),
    content: @Composable RowScope.() -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = colors,
        enabled = enabled,
        elevation = elevation,
        shape = shape,
        border = border,
        content = content
    )
}
