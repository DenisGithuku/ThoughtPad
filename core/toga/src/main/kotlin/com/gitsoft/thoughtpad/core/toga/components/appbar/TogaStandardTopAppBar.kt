
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
package com.gitsoft.thoughtpad.core.toga.components.appbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import com.gitsoft.thoughtpad.core.toga.components.icon.TogaNavigationIcon
import com.gitsoft.thoughtpad.core.toga.components.text.TogaMediumTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TogaStandardTopAppBar(
    title: String? = null,
    onNavigateBack: () -> Unit,
    actions: @Composable RowScope.() -> Unit,
    colors: TopAppBarColors =
        TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
) {
    CenterAlignedTopAppBar(
        title = {
            if (title != null) {
                TogaMediumTitle(text = title)
            }
        },
        navigationIcon = { TogaNavigationIcon(onNavigateBack = onNavigateBack) },
        actions = actions,
        colors = colors
    )
}
