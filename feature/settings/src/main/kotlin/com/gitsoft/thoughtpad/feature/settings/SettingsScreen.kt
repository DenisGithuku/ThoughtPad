
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
package com.gitsoft.thoughtpad.feature.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.gitsoft.thoughtpad.core.model.ThemeConfig
import core.gitsoft.thoughtpad.core.toga.components.dialog.TogaContentDialog
import core.gitsoft.thoughtpad.core.toga.components.scaffold.TogaStandardScaffold
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsRoute(onNavigateBack: () -> Unit, viewModel: SettingsViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    SettingsScreen(
        state = state,
        onToggleTheme = viewModel::onToggleTheme,
        onToggleThemeDialog = viewModel::onToggleThemeDialog,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsScreen(
    state: SettingsUiState,
    onToggleTheme: (ThemeConfig) -> Unit,
    onToggleThemeDialog: (Boolean) -> Unit,
    onNavigateBack: () -> Unit
) {
    TogaStandardScaffold(title = R.string.settings, onNavigateBack = onNavigateBack) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            AnimatedVisibility(
                visible = state.isThemeDialogShown,
                enter =
                    fadeIn(spring(stiffness = Spring.StiffnessHigh)) +
                        scaleIn(
                            initialScale = .8f,
                            animationSpec =
                                spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessMediumLow
                                )
                        ),
                exit = slideOutVertically { it / 8 } + fadeOut() + scaleOut(targetScale = .95f)
            ) {
                TogaContentDialog(
                    title = { Text(text = "Select theme", style = MaterialTheme.typography.headlineSmall) },
                    content = {
                        Column(modifier = Modifier.testTag(TestTags.THEME_COLUMN).fillMaxWidth()) {
                            state.availableThemes.forEach { availableTheme ->
                                Row(
                                    modifier =
                                        Modifier.clickable(
                                                role = Role.RadioButton,
                                                enabled = true,
                                                onClick = { onToggleTheme(availableTheme) }
                                            )
                                            .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    RadioButton(
                                        selected = availableTheme == state.selectedTheme,
                                        onClick = { onToggleTheme(availableTheme) }
                                    )
                                    Text(
                                        text = availableTheme.name.lowercase().replaceFirstChar { it.uppercase() },
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    },
                    onDismissRequest = { onToggleThemeDialog(false) }
                )
            }

            SettingsItem(
                leading = {
                    Icon(
                        painter =
                            painterResource(
                                id =
                                    if (state.selectedTheme == ThemeConfig.DARK) {
                                        R.drawable.ic_dark_mode
                                    } else R.drawable.ic_light_mode
                            ),
                        contentDescription = "Theme"
                    )
                },
                title = {
                    Text(
                        text = stringResource(R.string.theme_title),
                        style =
                            MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.onSurface)
                    )
                },
                description = {
                    Text(
                        text =
                            stringResource(
                                when (state.selectedTheme) {
                                    ThemeConfig.LIGHT -> R.string.theme_light
                                    ThemeConfig.DARK -> R.string.theme_dark
                                }
                            ),
                        style =
                            MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                    )
                },
                onClick = { onToggleThemeDialog(true) }
            )
        }
    }
}

@Composable
fun SettingsItem(
    modifier: Modifier = Modifier,
    leading: (@Composable () -> Unit)? = null,
    title: @Composable () -> Unit,
    description: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable(enabled = onClick != null) { onClick?.let { it() } }
                .padding(16.dp)
                .testTag(TestTags.SETTINGS_ITEM),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (leading != null) leading()
            Column {
                title()
                if (description != null) description()
            }
        }
        if (trailing != null) trailing()
    }
}

internal object TestTags {
    const val SETTINGS_ITEM = "settings_item"
    const val THEME_COLUMN = "theme_column"
}
