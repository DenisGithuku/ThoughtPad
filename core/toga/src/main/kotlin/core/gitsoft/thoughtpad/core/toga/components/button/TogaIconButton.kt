
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
package core.gitsoft.thoughtpad.core.toga.components.button

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

@Composable
fun TogaIconButton(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    @StringRes contentDescription: Int,
    tint: Color = MaterialTheme.colorScheme.onBackground,
    onClick: () -> Unit
) {
    IconButton(modifier = modifier, onClick = onClick) {
        Icon(
            painter = painterResource(icon),
            contentDescription = stringResource(contentDescription),
            tint = tint
        )
    }
}