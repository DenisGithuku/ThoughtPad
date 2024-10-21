
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
package core.gitsoft.thoughtpad.core.toga.components.appbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import core.gitsoft.thoughtpad.core.toga.components.icon.TogaNavigationIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TogaStandardTopAppBar(
    title: String,
    onNavigateBack: () -> Unit,
    actions: @Composable RowScope.() -> Unit,
    colors: TopAppBarColors =
        TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
) {
    CenterAlignedTopAppBar(
        title = { Text(text = title, style = MaterialTheme.typography.titleMedium) },
        navigationIcon = { TogaNavigationIcon(onNavigateBack = onNavigateBack) },
        actions = actions,
        colors = colors
    )
}
